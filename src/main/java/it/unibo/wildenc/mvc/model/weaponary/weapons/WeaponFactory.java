package it.unibo.wildenc.mvc.model.weaponary.weapons;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon.WeaponStats;

public class WeaponFactory {

    public static Weapon basicAttack() {
        return new GenericWeapon(
            1.0,
            1,
            1,
            "BasicProj",
            2.0,
            (pos, info) -> new Vector2d(
                pos.x() + info.atkDirection().x * info.atkVelocity(),
                pos.y() + info.atkDirection().y * info.atkVelocity()
            ),
            (lvl, stats) -> new WeaponStats( // It's bad, should redo.
                stats.weaponCooldown() - lvl * 0.05,
                stats.projDamage() + lvl * 1.5,
                stats.projVelocity() + lvl * 0.5,
                stats.projID(),
                stats.hbRadius(),
                stats.moveFunction(),
                stats.upgradeLogics()
            )
        );
    }
}
