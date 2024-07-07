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
import org.vaadin.crm.entities.Task;
import org.vaadin.crm.entities.Company;
import org.vaadin.crm.entities.Contact;
import org.vaadin.crm.entities.Status;
import org.vaadin.crm.services.CrmService;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Route(value = "form1")
public class ContactForm1 extends FormLayout {
    CrmService service;

    TextField firstName = new TextField("Имя");
    TextField lastName = new TextField("Фамилия");

    DateTimePicker dateTime = new DateTimePicker("Start date");

    EmailField email = new EmailField("Email");
    ComboBox<Status> status = new ComboBox<>("Статус");
    ComboBox<Company> company = new ComboBox<>("Компания");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Закрыть");

    BeanValidationBinder<Contact> binder = new BeanValidationBinder<>(Contact.class);
    //BeanValidationBinder<Task> taskBinder = new BeanValidationBinder<>(Task.class);

    public ContactForm1(List<Company> companies, List<Status> statuses, CrmService service) {
        this.service = service;

        addClassName("contact-form");
        binder.bindInstanceFields(this);
        //taskBinder.bindInstanceFields(this);

        company.setItems(companies);
        company.setItemLabelGenerator(Company::getName);
        status.setItems(statuses);
        status.setItemLabelGenerator(Status::getName);

        dateTime.setValue(LocalDateTime.now().withNano(0));

        add(firstName,
                lastName,
                email,
                company,
                status,
                dateTime,
                createButtonsLayout());

        addSaveListener(e -> saveContact(e));
        addDeleteListener(this::deleteContact);
    }

    public void setContact(Contact contact) {
        binder.setBean(contact);
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
        private Contact contact;

        protected ContactFormEvent(ContactForm1 source, Contact contact) {
            super(source, false);
            this.contact = contact;
        }
        public Contact getContact() {
            return contact;
        }
    }

    public static class SaveEvent extends ContactFormEvent {
        SaveEvent(ContactForm1 source, Contact contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends ContactFormEvent {
        DeleteEvent(ContactForm1 source, Contact contact) {
            super(source, contact);
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
        service.saveContact(event.getContact());
    }

    private void deleteContact(ContactForm1.DeleteEvent event) {
        service.deleteContact(event.getContact());

    }


}




