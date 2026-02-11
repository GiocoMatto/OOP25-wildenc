package it.unibo.wildenc.mvc.model.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.map.MapTestingConstants;

/**
 * Test class for {@link PlayerImpl}.
 */
class PlayerTest {

    private PlayerImpl player = MapTestingConstants.TestObject.PlayerObject.getAsPlayer();

    @Test
    void testInitialization() {
        //verifico i valori iniziali
        assertEquals(100, player.getExpToNextLevel(), "Al livello 1 servono 100 xp");
        assertEquals(0, player.getExp(), "L'esperienza iniziale deve essere 0");
        assertEquals(0, player.getMoney(), "I soldi iniziali devono essere 0");
    }

    @Test
    void testAddExpNoLevelUp() {
        // aggiungo 50 exp, livvelo resta 1 
        player.addExp(50);
        
        assertEquals(50, player.getExp());
        assertEquals(100, player.getExpToNextLevel());
    }

    @Test
    void testLevelUpExact() {
        //aggiungi 100 exp, salgo al liv 2
        player.addExp(100);

        //arrivato al liv 2 l'exp torna a 0
        assertEquals(0, player.getExp());
        
        //ora servono 200 exp per salire di liv
        assertEquals(200, player.getExpToNextLevel());
    }

    @Test
    void testLevelUpOverflow() {
        //aggiungo 150 exp, salgo al liv 2 e mi avanzano 50 exp
        player.addExp(150);

        //check livello 2 controllando quanta exp mi manca per salire al liv 3
        assertEquals(200, player.getExpToNextLevel());
        
        //deve avanzare 50exp
        assertEquals(50, player.getExp());
    }

    @Test
    void testMultiLevelUp() {
        //parto dal livello 1 e aggiungo 350 xp
        //devo quindi arrivare al liv 3 con 50 exp rimasti
        player.addExp(350);

        assertEquals(50, player.getExp());
        
        // al liv 3 la prossima soglia è 3 * 100 = 300
        assertEquals(300, player.getExpToNextLevel());
    }

    @Test
    void testMoney() {
        player.addMoney(50);
        assertEquals(50, player.getMoney());

        player.addMoney(100);
        assertEquals(150, player.getMoney());
    }

    @Test
    void testHeal() {
        //MapTestingCommons crea il player con 100hp
        assertEquals(100, player.getCurrentHealth());

        //Curo a vita piena (non deve superare 100)
        player.heal(50);
        assertEquals(100, player.getCurrentHealth());
    }
}