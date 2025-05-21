/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.votingsystems;

import javax.swing.SwingUtilities;

import com.mycompany.votingsystems.gui.LoginScreen;

/**
 *
 * @author eirmo
 */
public class VotingSystems {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        });
    }
}
