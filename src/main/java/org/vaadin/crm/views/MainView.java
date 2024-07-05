package org.vaadin.crm.views;

/**
 * A sample Vaadin view class.
 * <p>
 * To implement a Vaadin view just extend any Vaadin component and use @Route
 * annotation to announce it in a URL as a Spring managed bean.
 * <p>
 * A new instance of this class is created for every new user and every browser
 * tab/window.
 * <p>
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message in a notification.
 */

//import com.example.application.data.Contact;
//import com.vaadin.flow.component.Component;
import com.example.application.views.list.ContactForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.crm.entities.Contact;
import org.vaadin.crm.services.CrmService;

import java.util.Collections;

@Route(value = "")
@PageTitle("Контакты | Sky CRM")
public class MainView extends VerticalLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;
    ContactForm form1;
    CrmService service;

    public MainView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureForm1();
        getTabs();

        add(getToolbar(), getTabs());
        updateList();
        closeEditor();

    }

    private Component getTabs() {
        TabSheet tabSheet = new TabSheet();
        tabSheet.setSizeFull();
        tabSheet.setHeight("800px");

        tabSheet.add("Контакты", getContent());
        tabSheet.add("Подробнее", form1);
        tabSheet.add("Документы",
                new Div(new Text("This is the Shipping tab content")));

        tabSheet.addSelectedChangeListener(e -> {
            if(tabSheet.getSelectedIndex() == 1) {
                form1.setContact(grid.asSingleSelect().getValue()); // binds the form fields with the object fields: binder.setBean(contact);
                //form1.setVisible(true);
                addClassName("editing1");
            }
            if(tabSheet.getSelectedIndex() == 0) {
                System.out.println("1");
            }
        });
        return tabSheet;
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "email");

        grid.getColumns().get(0).setHeader("Имя");
        grid.getColumns().get(1).setHeader("Фамилия");

        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Статус");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Компания");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");

        form.addSaveListener(this::saveContact);
        form.addDeleteListener(this::deleteContact);
        form.addCloseListener(e -> closeEditor());
    }

    private void configureForm1() {
        form1 = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form1.setWidth("25em");

        form1.addSaveListener(this::saveContact);
        form1.addDeleteListener(this::deleteContact);
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Сортировать по алфавиту...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Добавить контакт");
        addContactButton.addClickListener(click -> addContact());

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    public void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact); // binds the form fields with the object fields: binder.setBean(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}