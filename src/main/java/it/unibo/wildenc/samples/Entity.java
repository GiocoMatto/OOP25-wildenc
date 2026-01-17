package it.unibo.wildenc.samples;

import java.util.List;

public interface Entity extends Movable {

    int getHealth();

    boolean takeDamage(int dmg);

    List<Weapon> getWeapons();

}
