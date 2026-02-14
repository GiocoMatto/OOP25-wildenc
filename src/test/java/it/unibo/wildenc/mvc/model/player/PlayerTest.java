package it.unibo.wildenc.mvc.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void testInitialization() {
        //verifico i valori iniziali
        assertEquals(EXP_100, player.getExpToNextLevel(), "Al livello 1 servono 100 xp");
        assertEquals(0, player.getExp(), "L'esperienza iniziale deve essere 0");
        assertEquals(0, player.getMoney(), "I soldi iniziali devono essere 0");
    }

    @Test
    void testAddExpNoLevelUp() {
        // aggiungo 50 exp, livvelo resta 1 
        player.addExp(EXP_50);

        assertEquals(EXP_50, player.getExp());
        assertEquals(EXP_100, player.getExpToNextLevel());
    }

    @Test
    void testLevelUpExact() {
        //aggiungi 100 exp, salgo al liv 2
        player.addExp(100);

        //arrivato al liv 2 l'exp torna a 0
        assertEquals(0, player.getExp());

        //ora servono 200 exp per salire di liv
        assertEquals(EXP_200, player.getExpToNextLevel());
    }

    @Test
    void testLevelUpOverflow() {
        //aggiungo 150 exp, salgo al liv 2 e mi avanzano 50 exp
        player.addExp(EXP_150);

        //check livello 2 controllando quanta exp mi manca per salire al liv 3
        assertEquals(EXP_200, player.getExpToNextLevel());

        //deve avanzare 50exp
        assertEquals(EXP_50, player.getExp());
    }

    @Test
    void testMultiLevelUp() {
        //parto dal livello 1 e aggiungo 350 xp
        //devo quindi arrivare al liv 3 con 50 exp rimasti
        player.addExp(EXP_350);

        assertEquals(EXP_50, player.getExp());

        // al liv 3 la prossima soglia è 3 * 100 = 300
        assertEquals(EXP_300, player.getExpToNextLevel());
    }

    @Test
    void testMoney() {
        player.addMoney(MONEY_50);
        assertEquals(MONEY_50, player.getMoney());

        player.addMoney(MONEY_100);
        assertEquals(MONEY_150, player.getMoney());
    }

    @Test
    void testHeal() {
        //MapTestingCommons crea il player con 100hp
        assertEquals(MAX_HEALTH, player.getCurrentHealth());

        //Curo a vita piena (non deve superare 100)
        player.heal(HEAL_50);
        assertEquals(MAX_HEALTH, player.getCurrentHealth());
    }
}
