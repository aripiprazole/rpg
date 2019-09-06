package me.lorenzo.rpg.manager;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import me.lorenzo.rpg.Main;
import net.eduard.api.lib.manager.DBManager;

public class Account {

    private String nome;
    private int id;
    private int characterLimit = 10;
    private ArrayList<Character> characters = new ArrayList<Character>();
    private int actualCharacterId = -1;
    private boolean staff;//isso aqui, achoq vou colocar numa enum e colocar cargos

    public boolean haveSelectedCharacter() {
        return getActualCharacter() != null;
    }

    public void selectCharacter(Character character) {
        actualCharacterId = character.getId();
    }

    public Character getActualCharacter() {
        for (Character character : characters) {
            if (character.getId() == actualCharacterId) {
                return character;
            }
        }
        return characters.stream().filter(character -> character.getId() == actualCharacterId).findFirst().orElse(null);
    }

    public Character createCharacter(RpgClass rpgClass) {
        Character character = new Character();
        character.setRpgClass(rpgClass);
        characters.add(character);
        DBManager db = Main.getManager().getDatabase();
        int idDoPersonagem = db.insert("characters", getId(), rpgClass, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, null, "", "");
        character.setId(idDoPersonagem);
        character.setInventory(new ItemStack[4 * 9]);
        db.insert("personagens_profissoes", character.getId(), 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0);
        return character;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return nome;
    }

    public void setName(String nome) {
        this.nome = nome;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public int getCharacterAmount() {
        return characters.size();
    }

    public int getCharacterLimit() {
        return characterLimit;
    }

    public void setCharacterLimit(int characterLimit) {
        this.characterLimit = characterLimit;
    }

    public int getActualCharacterId() {
        return actualCharacterId;
    }

    public void setActualCharacterId(int personagemAtualId) {
        this.actualCharacterId = personagemAtualId;
    }

    public boolean isStaff() {
        return staff;
    }

    public void setStaff(boolean staff) {
        this.staff = staff;
    }

    public void deselectCharacter() {
        this.actualCharacterId = -1;
    }

}
