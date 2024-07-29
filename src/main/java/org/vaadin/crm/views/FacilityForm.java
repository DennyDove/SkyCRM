package org.vaadin.crm.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.shared.Registration;
import org.vaadin.crm.entities.Facility;
import org.vaadin.crm.entities.Status;
import org.vaadin.crm.services.CrmService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

//@Route(value = "form")
public class FacilityForm extends FormLayout {
    CrmService service;

    TextField facilityName = new TextField("Название объекта");
    TextField area = new TextField("Площадь, м2");
    TextField address = new TextField("Адрес");
    TextField price = new TextField("Стоимость");
    TextField owner = new TextField("Арендатор, коммерческие условия");
    TextField facilityReadiness = new TextField("Стадия готовности");
    TextArea description = new TextArea("Описание объекта");
    TextField contactLinks = new TextField("Контакты/ссылки");

    DateTimePicker dateTime = new DateTimePicker("Дата создания");
    ComboBox<Status> status = new ComboBox<>("Статус");



    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    static Long id;

    BeanValidationBinder<Facility> binder = new BeanValidationBinder<>(Facility.class);
    //BeanValidationBinder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    public FacilityForm(CrmService service, List<Status> statuses) {
        this.service = service;

        addClassName("facility-form");
        binder.bindInstanceFields(this);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        dateTime.setValue(LocalDateTime.now().withNano(0));

        description.addClassName("description-area");
        //description.setHeight(77, Unit.PIXELS);
        description.setMaxHeight("120px");

        add(facilityName,
                area,
                address,
                price,
                owner,
                facilityReadiness,
                description,
                contactLinks,
                status,
                dateTime,
                createButtonsLayout());

        uploadBasic();
        addSaveListener(e -> saveFacility(e));
        addDeleteListener(this::deleteFacility);
        addCloseListener(e -> closeEditor());
    }

    public void setFacility(Facility facility) {
        binder.setBean(facility);
    }

    private void uploadBasic() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);

            System.out.println(id);

            System.out.println(fileName);

            File path = new File("/facility/"+id);
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

        // Закомментил кнопку ENTER, т.к. это входит в конфликт с заполнением поля "description"
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

    public static abstract class FacilityFormEvent extends ComponentEvent<FacilityForm> {
        private Facility facility;

        protected FacilityFormEvent(FacilityForm source, Facility facility) {
            super(source, false);
            this.facility = facility;
        }
        public Facility getFacility() {
            return facility;
        }
    }

    public static class SaveEvent extends FacilityFormEvent {
        SaveEvent(FacilityForm source, Facility facility) {
            super(source, facility);
        }
    }

    public static class DeleteEvent extends FacilityFormEvent {
        DeleteEvent(FacilityForm source, Facility facility) {
            super(source, facility);
        }

    }

    public static class CloseEvent extends FacilityFormEvent {
        CloseEvent(FacilityForm source) {
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



    private void saveFacility(FacilityForm.SaveEvent event) {
        service.saveFacility(event.getFacility());

    }

    private void deleteFacility(FacilityForm.DeleteEvent event) {
        service.deleteFacility(event.getFacility());

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
        this.setFacility(null);
        this.remove();
        UI ui = this.getUI().get();
        ui.refreshCurrentRoute(true);
    }


}




