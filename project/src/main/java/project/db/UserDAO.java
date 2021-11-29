/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project.db;

import java.util.ArrayList;
import project.domain.UserInterface;

/**
 *
 * @author Heli
 */
public interface UserDAO {

    /**
     * Tallentaa käyttäjän tietokantaan.
     * @param user lisättävä olio
     *  @throws Exception
     */
    void add(UserInterface user);

    ArrayList<UserInterface> fetchAllUsers();

    /**
     * Hae käyttäjän tiedot tietokannasta.
     * @throws Exception
     */
    boolean login(UserInterface user) throws Exception;

    /**
     * Poistaa käyttäjän tietokannasta.
     * @param user poistettava olio
     * @throws Exception
     */
    void remove(UserInterface user) throws Exception;
    
}
