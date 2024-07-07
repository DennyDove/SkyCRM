package org.vaadin.crm.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.crm.TestView;
import org.vaadin.crm.entities.Contact;
import org.vaadin.crm.services.CrmService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@Route("customers")

public class CustomersView extends AppLayout {
    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
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

        addToDrawer(scroller);
        addToNavbar(toggle, title, filterText);
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

    private SideNav getTabs() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Orders", "/orders",
                        VaadinIcon.CART.create()),
                new SideNavItem("Customers", "/customers",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Products", "/products",
                        VaadinIcon.PACKAGE.create()),
                new SideNavItem("Documents", "/documents",
                        VaadinIcon.RECORDS.create()),
                new SideNavItem("Tasks", "/tasks",
                        VaadinIcon.LIST.create()),
                new SideNavItem("Analytics", "/analytics",
                        VaadinIcon.CHART.create()));
        return nav;
    }
    // tag::snippet[]

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("dateTime", "firstName", "lastName", "email");

        grid.getColumns().get(0).setHeader("Дата");
        grid.getColumns().get(1).setHeader("Имя");
        grid.getColumns().get(2).setHeader("Фамилия");

        grid.addColumn(contact -> contact.getStatus().getName()).setHeader("Статус");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Компания");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        //grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));
        grid.addItemDoubleClickListener(e -> {
            grid.getUI().ifPresent(ui -> ui.navigate(ContactForm1.class).ifPresent(form -> {
                form.setContact(grid.asSingleSelect().getValue());
                if(form.dateTime.isEmpty()) {
                    form.dateTime.setValue(LocalDateTime.now().withNano(0));
                }
            }));
        });
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
    }

    private void updateList() {

        grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}
