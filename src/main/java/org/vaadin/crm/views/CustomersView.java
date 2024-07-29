package org.vaadin.crm.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.crm.entities.Company;
import org.vaadin.crm.services.CrmService;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Route("customers")

public class CustomersView extends AppLayout {
    Grid<Company> grid = new Grid<>(Company.class);
    TextField filterText = new TextField();
    Button but1 = new Button("Add file");
    Long id;

    //ContactForm1 form1;

    CompanyDialog companyDialog;

    CrmService service;

    public CustomersView(CrmService service) {
        this.service = service;

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Customers");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "10");

        SideNav nav = getTabs();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        Button addContactButton = new Button("Добавить контакт");
        addContactButton.addClickListener(click -> addContact());

        but1.addClickListener(click -> {

            id = grid.asSingleSelect().getValue().getId();

            //String fileName = "/path"+id;

            File path = new File("/documents/"+id);

            Desktop desktop = null;
            desktop = Desktop.getDesktop();

            try {
                desktop.open(path.getAbsoluteFile());
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        addToDrawer(scroller);
        addToNavbar(toggle, title, filterText, but1, addContactButton);

        filterText.setPlaceholder("Сортировать по алфавиту...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        setPrimarySection(Section.DRAWER);

        configureGrid();
        setContent(grid);
        updateList();
    }
    // end::snippet[]

    /*
    private void configureDialog() {
        form1 = new ContactForm1(service.findAllStatuses(), service);
        form1.setWidth("25em");

        form1.addSaveListener(this::saveContact);
        form1.addDeleteListener(this::deleteContact);
    }
    */

    private SideNav getTabs() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Объекты", "/facilities",
                        VaadinIcon.CART.create()),
                new SideNavItem("Компании", "/customers",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Documents", "/documents",
                        VaadinIcon.RECORDS.create()),
                new SideNavItem("Tasks", "/tasks",
                        VaadinIcon.LIST.create()));
        return nav;
    }
    // tag::snippet[]

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("dateTime", "companyName", "email");

        grid.getColumns().get(0).setHeader("Дата");
        grid.getColumns().get(1).setHeader("Название компании");

        grid.addColumn(company -> company.getStatus().getName()).setHeader("Статус");
        grid.addColumn(Company::getComments).setHeader("Комментарии");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
        //grid.addSelectionListener(e -> {});
        //grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        grid.addItemDoubleClickListener(e -> {
            companyDialog = new CompanyDialog(service, grid.asSingleSelect().getValue());
            ContactForm.id = grid.asSingleSelect().getValue().getId();
            addToNavbar(companyDialog);
        });

        // установить распределение текста в ячейке по высоте
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getCompany());
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getCompany());
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        companyDialog = new CompanyDialog(service);
        //dialogForm.setVisible(true);
        addToNavbar(companyDialog);
    }

    private void updateList() {

        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}
