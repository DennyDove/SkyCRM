package org.vaadin.crm.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.crm.entities.Company;
import org.vaadin.crm.entities.Status;
import org.vaadin.crm.services.CrmService;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

//@Route(value = "form")
public class ContactForm extends FormLayout {
    CrmService service;

    TextField companyName = new TextField("Название компании");

    DateTimePicker dateTime = new DateTimePicker("Дата создания");

    EmailField email = new EmailField("Email");
    ComboBox<Status> status = new ComboBox<>("Статус");

    TextArea comments = new TextArea("Комментарии");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    static Long id;

    BeanValidationBinder<Company> binder = new BeanValidationBinder<>(Company.class);
    //BeanValidationBinder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    public ContactForm(CrmService service, List<Status> statuses) {
        this.service = service;

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        //taskBinder.bindInstanceFields(this);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        dateTime.setValue(LocalDateTime.now().withNano(0));

        comments.addClassName("comments-area");
        //comments.setHeight(77, Unit.PIXELS);
        //comments.setMinHeight("100px");
        comments.setMaxHeight("120px");


        add(companyName,
                email,
                status,
                dateTime,
                comments,
                createButtonsLayout());

        uploadBasic();
        addSaveListener(e -> saveContact(e));
        addDeleteListener(this::deleteContact);
        addCloseListener(e -> closeEditor());
    }

    public void setCompany(Company company) {
        binder.setBean(company);
    }

    private void uploadBasic() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            System.out.println(id);

            System.out.println(fileName);

            File path = new File("/documents/"+id);
            path.mkdirs();

            System.out.println(path);

            try (FileOutputStream output = new FileOutputStream(path+"/"+fileName)) { // try-with-resources
                output.write(inputStream.readAllBytes());

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Do something with the file data
            // processFile(inputStream, fileName);
        });

        add(upload);
    }


    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Закомментил кнопку ENTER, т.к. это входит в конфликт с заполнением поля "comments"
        //save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> deleteAndClose());
        close.addClickListener(e -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
        closeEditor();
    }

    void deleteAndClose() {
        ConfirmDialog confirm = new ConfirmDialog();
        confirm.setWidth("233px");
        confirm.setHeader("Удалить контакт?");
        //confirm.setConfirmText("Удалить контакт?");
        confirm.open();
        confirm.setConfirmButton("Да", e-> {
            if(binder.isValid()) {
                fireEvent(new DeleteEvent(this, binder.getBean()));
            }
            closeEditor();
        });
        confirm.setCancelButton("Нет", e-> {
            confirm.close();
        });

    }

    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm> {
        private Company company;

        protected ContactFormEvent(ContactForm source, Company company) {
            super(source, false);
            this.company = company;
        }
        public Company getCompany() {
            return company;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm source, Company company) {
            super(source, company);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm source, Company company) {
            super(source, company);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }



    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getCompany());

    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getCompany());

    }

    /*
    public void editContact(Company company) {
        if (company == null) {
            closeEditor();
        } else {
            form.setContact(contact); // binds the form fields with the object fields: binder.setBean(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }
    */

    private void closeEditor() {
        this.setCompany(null);
        this.remove();
        UI ui = this.getUI().get();
        ui.refreshCurrentRoute(true);
    }


}




