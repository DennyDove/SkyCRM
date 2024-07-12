package org.vaadin.crm.views;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.shared.Registration;
import org.vaadin.crm.entities.Company;
import org.vaadin.crm.entities.Status;
import org.vaadin.crm.services.CrmService;


import java.time.LocalDateTime;
import java.util.List;

@Route(value = "form1")
public class ContactForm1 extends FormLayout {
    CrmService service;

    TextField companyName = new TextField("Название компании");

    DateTimePicker dateTime = new DateTimePicker("Дата создания");

    EmailField email = new EmailField("Email");
    ComboBox<Status> status = new ComboBox<>("Статус");

    TextField comments = new TextField("Комментарии");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    BeanValidationBinder<Company> binder = new BeanValidationBinder<>(Company.class);
    //BeanValidationBinder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    public ContactForm1(List<Status> statuses, CrmService service) {
        this.service = service;

        addClassName("contact-form");
        binder.bindInstanceFields(this);

        //taskBinder.bindInstanceFields(this);

        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        dateTime.setValue(LocalDateTime.now().withNano(0));

        add(companyName,
                email,
                status,
                dateTime,
                comments,
                createButtonsLayout());

        addSaveListener(e -> saveContact(e));
        addDeleteListener(this::deleteContact);
    }

    public void setCompany(Company company) {
        binder.setBean(company);
    }


    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(e -> validateAndSave());
        delete.addClickListener(e -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(e -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    void validateAndSave() {
        if(binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class ContactFormEvent extends ComponentEvent<ContactForm1> {
        private Company company;

        protected ContactFormEvent(ContactForm1 source, Company company) {
            super(source, false);
            this.company = company;
        }
        public Company getCompany() {
            return company;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm1 source, Company company) {
            super(source, company);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm1 source, Company company) {
            super(source, company);
        }

    }

    public static class CloseEvent extends ContactFormEvent {
        CloseEvent(ContactForm1 source) {
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


    private void saveContact(ContactForm1.SaveEvent event) {
        service.saveContact(event.getCompany());
    }

    private void deleteContact(ContactForm1.DeleteEvent event) {
        service.deleteContact(event.getCompany());

    }


}




