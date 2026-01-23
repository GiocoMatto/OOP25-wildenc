package it.unibo.wildenc.mvc.model;

import java.util.List;

public interface Entity extends Movable {

    int getHealth();

    boolean takeDamage(int dmg);

    List<Weapon> getWeapons();

}
