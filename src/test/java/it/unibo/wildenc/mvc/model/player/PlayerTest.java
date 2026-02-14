package it.unibo.wildenc.mvc.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.map.MapTestingConstants;

/**
 * Test class for {@link PlayerImpl}.
 */
class PlayerTest {

    private static final int EXP_50 = 50;
    private static final int EXP_100 = 100;
    private static final int EXP_150 = 150;
    private static final int EXP_200 = 200;
    private static final int EXP_300 = 300;
    private static final int EXP_350 = 350;
    private static final int MONEY_50 = 50;
    private static final int MONEY_100 = 100;
    private static final int MONEY_150 = 150;
    private static final int HEAL_50 = 50;
    private static final int MAX_HEALTH = 100;

    private PlayerImpl player = MapTestingConstants.TestObject.PLAYEROBJECT.getAsPlayer();

    /**
     * Test initialization values.
     */
    @Test
    void testInitialization() {
        assertEquals(EXP_100, player.getExpToNextLevel(), "Al livello 1 servono 100 xp");
        assertEquals(0, player.getExp(), "L'esperienza iniziale deve essere 0");
        assertEquals(0, player.getMoney(), "I soldi iniziali devono essere 0");
    }

    /**
     * Test adding exp without level up.
     */
    @Test
    void testAddExpNoLevelUp() {
        player.addExp(EXP_50);

        assertEquals(EXP_50, player.getExp());
        assertEquals(EXP_100, player.getExpToNextLevel());
    }

    /**
     * Test exact level up.
     */
    @Test
    void testLevelUpExact() {
        player.addExp(EXP_100);

        assertTrue(player.canLevelUp());
        player.levelUp();

        assertEquals(0, player.getExp());
        assertEquals(EXP_200, player.getExpToNextLevel());
    }

    /**
     * Test level up overflow.
     */
    @Test
    void testLevelUpOverflow() {
        player.addExp(EXP_150);

        assertTrue(player.canLevelUp());
        player.levelUp();

        assertEquals(EXP_200, player.getExpToNextLevel());
        assertEquals(EXP_50, player.getExp());
    }

    /**
     * Test multi level up.
     */
    @Test
    void testMultiLevelUp() {
        player.addExp(EXP_350);

        while (player.canLevelUp()) {
            player.levelUp();
        }

        assertEquals(EXP_50, player.getExp());
        assertEquals(EXP_300, player.getExpToNextLevel());
    }

    /**
     * Test money.
     */
    @Test
    void testMoney() {
        player.addMoney(MONEY_50);
        assertEquals(MONEY_50, player.getMoney());

        player.addMoney(MONEY_100);
        assertEquals(MONEY_150, player.getMoney());
    }

    /**
     * Test healing.
     */
    @Test
    void testHeal() {
        assertEquals(MAX_HEALTH, player.getCurrentHealth());

        player.heal(HEAL_50);
        assertEquals(MAX_HEALTH, player.getCurrentHealth());
    }
}
