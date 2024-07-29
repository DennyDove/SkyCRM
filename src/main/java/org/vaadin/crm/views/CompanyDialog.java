package org.vaadin.crm.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import org.vaadin.crm.entities.Company;
import org.vaadin.crm.services.CrmService;

import java.time.LocalDateTime;

//@Route("dialog-basic")
public class CompanyDialog extends Div {

    public CompanyDialog(CrmService service) {
        Dialog dialog = new Dialog();
        addClassName("popup-window");

        ContactForm form = new ContactForm(service, service.findAllStatuses());
        form.setCompany(new Company());
        form.dateTime.setValue(LocalDateTime.now().withNano(0));

        //Стили всплывающего окна
        dialog.setHeaderTitle("Создать новый контакт");
        dialog.setHeight("777px");
        dialog.setWidth("523px");
        dialog.setMaxWidth("100%");


        //VerticalLayout dialogLayout = createDialogLayout();

        dialog.add(form);
        dialog.addDialogCloseActionListener(e -> {
            if(dialog.isCloseOnOutsideClick()) {
                UI ui = this.getUI().get();
                ui.refreshCurrentRoute(true);
            }
        });

        add(dialog);

        dialog.open();

    }

    public CompanyDialog(CrmService service, Company company) {
        Dialog dialog = new Dialog();
        addClassName("popup-window");

        ContactForm form = new ContactForm(service, service.findAllStatuses());
        form.setCompany(company);
        if(form.dateTime.isEmpty()) {
            form.dateTime.setValue(LocalDateTime.now().withNano(0));
        }

        dialog.setHeaderTitle("Редактировать данные");

        //VerticalLayout dialogLayout = createDialogLayout();

        dialog.add(form);
        dialog.addDialogCloseActionListener(e -> {
                    if(dialog.isCloseOnOutsideClick()) {
                        UI ui = this.getUI().get();
                        ui.refreshCurrentRoute(true);
                    }
        });

        /*
        Button saveButton = createSaveButton(dialog);
        Button cancelButton = new Button("Cancel", e -> dialog.close());
        dialog.getFooter().add(cancelButton);
        dialog.getFooter().add(saveButton);
        */

        //Button button = new Button("Show dialog", e -> dialog.open());

        add(dialog);

        dialog.open();
        // Center the button within the example
        /*
        getStyle().set("position", "fixed").set("top", "0").set("right", "0")
                .set("bottom", "0").set("left", "0").set("display", "flex")
                .set("align-items", "center").set("justify-content", "center")
                .set("height", "77px").set("width", "12rem");

        */
    }

    /*
    private static VerticalLayout createDialogLayout() {

        TextField firstNameField = new TextField("First name");
        TextField lastNameField = new TextField("Last name");

        VerticalLayout dialogLayout = new VerticalLayout(firstNameField,
                lastNameField);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "12rem").set("max-width", "100%");

        return dialogLayout;
    }

    private static Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Add", e -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }
    */

}