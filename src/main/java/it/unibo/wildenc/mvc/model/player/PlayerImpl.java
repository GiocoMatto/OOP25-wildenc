package it.unibo.wildenc.samples;

import java.util.ArrayList;
import java.util.List;

public class PlayerImpl extends AbstractEntity implements Player {
    
    private int health;
    private int speed;
    private List<Weapon> weapons;
    private Point2D position;
    private Vector2D currentDirection;

    public PlayerImpl(Point2D startPosition, int speed) {
        
        this.position = startPosition;
        this.speed = speed;
        this.health = 100;
        this.weapons = new ArrayList<>(); 
        this.currentDirection = new Vector2D(0, 0);

    }

    //METODI!!

    @Override
    public void setDirection(Vector2D d) {
        
        this.currentDirection = d;
    }

    @Override
    public boolean moveTo(Point2D p) {
        
        this.position = p;
        return true;
    }
    
    @Override
    public int getHealth() {
        return this.health;

    }

    @Override
    public boolean takeDamage(int dmg) {
        this.health = this.health-dmg;
        return true;
    }

    @Override
    public List<Weapon> getWeapons() {
        return new ArrayList<>(this.weapons);

    }

    @Override
    public Point2D getPosition() {
        return this.position;

    }

    @Override
    public void levelUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'levelUp'");
    }

    @Override
    public void getExp(int exp) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExp'");
    }

}
