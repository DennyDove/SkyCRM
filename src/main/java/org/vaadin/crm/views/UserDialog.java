package org.vaadin.crm.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import org.vaadin.crm.entities.User;
import org.vaadin.crm.services.CrmService;

import java.time.LocalDateTime;

//@Route("dialog-basic")
public class UserDialog extends Div {

    public UserDialog(CrmService service) {
        Dialog dialog = new Dialog();
        addClassName("popup-user");

        UserForm form = new UserForm(service);
        form.setUser(new User());
        form.dateTime.setValue(LocalDateTime.now().withNano(0));

        //Стили всплывающего окна
        dialog.setHeaderTitle("Создать нового пользователя");
        dialog.setHeight("650px");
        dialog.setWidth("423px");
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
}