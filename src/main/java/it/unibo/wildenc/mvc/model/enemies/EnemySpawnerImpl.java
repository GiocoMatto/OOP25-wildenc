package it.unibo.wildenc.mvc.model.enemies;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.joml.Vector2d;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.EnemyFactory;
import it.unibo.wildenc.mvc.model.EnemySpawner;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;

import static it.unibo.wildenc.util.Utilities.pickRandom;
import static it.unibo.wildenc.util.Utilities.randomNameForRarity;

/**
 * Spawn enemy.
 */
public class EnemySpawnerImpl implements EnemySpawner {
    private static final int DS_X = 1200;
    private static final int DS_Y = 700;
    private static final int BASE_ENEMY = 10;
    private static final double ENEMY_LOAD_FACTOR = 2.4;
    private static final int PROBABILITY = 100;
    private static final int START_LIFE = 100;
    private static final double LIFE_LOAD_FACTOR = 2.6;
    private final Random rand;
    private final EnemyFactory enemyFactory;
    private long totalTime;

    /**
     * The gorup of enemy for calc probability.
     */
    public enum Group {
        COMMON(
            new Range(0, 40), 
            (ef, o, l, r) -> ef.closeRangeEnemy(o, l, randomNameForRarity(r))
        ),
        RARE(
            new Range(40, 65), 
            (ef, o, l, r) -> ef.closeRangeFastEnemy(o, l, randomNameForRarity(r))
        ),
        REAL_RARE(
            new Range(65, 80), 
            (ef, o, l, r) -> ef.rangedEnemy(o, l, randomNameForRarity(r))
        ),
        LEGEND(
            new Range(80, 90), 
            (ef, o, l, r) -> ef.rangedDoubleShotEnemy(o, l, randomNameForRarity(r))
        ),
        REAL_LEGEND(
            new Range(90, 97), 
            (ef, o, l, r) -> ef.roamingEnemy(o, l, randomNameForRarity(r))
        ),
        SUPER_LEGEND(
            new Range(97, 100), 
            (ef, o, l, r) -> ef.roamingLongLifeEnemy(o, l, randomNameForRarity(r))
        );

        private final Range range;
        private final EnemyCreator creator;

        Group(final Range range, final EnemyCreator creator) {
            this.range = range;
            this.creator = creator;
        }

        /**
         * Say if the Group contains the value of probability.
         * 
         * @param v value to check.
         * @return true: the Group has the win probability.
         */
        public boolean contains(final int v) {
            return range.contains(v);
        }

        /**
         * The enemy.
         * 
         * @param ef the factory.
         * @param origin the orin position.
         * @param life the life of enemy.
         * @return the new enemy.
         */
        public Enemy create(final EnemyFactory ef, final Vector2d origin, final int life) {
            return creator.create(ef, origin, life, name());
        }

        private interface EnemyCreator {
            Enemy create(EnemyFactory ef, Vector2d origin, int life, String rarity);
        }

        /**
         * The probability of spawn.
         * 
         * @param min min bownd include.
         * @param max max bound exclude.
         */
        private record Range(int min, int max) {
            public boolean contains(final int v) {
                return v >= min && v < max;
            }
        }

    }

    /**
     * Spawn different enemy.
     * 
     * @param target Comont target followed by all enemy.
     */
    public EnemySpawnerImpl(final MapObject target) {
        this.rand = new Random();
        this.enemyFactory = new EnemyFactoryImpl(target);
        this.totalTime = 0;
    }

    private Group fromProbability(final int v) {
        return Arrays.stream(Group.values())
            .filter(g -> g.contains(v))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Value out of range of probability: " + v));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Enemy> spawn(final Player p, final int enemyCount, final double tick) {
        final int target = BASE_ENEMY + (int) Math.round(Math.pow(p.getLevel(), ENEMY_LOAD_FACTOR));
        final int n = Math.max(0, target - enemyCount);
        this.totalTime = (int) (this.totalTime + tick);
        final int life = START_LIFE + (int) (totalTime * LIFE_LOAD_FACTOR);
        return IntStream.range(0, n)
            .mapToObj(num -> {
                final Vector2d origin = new Vector2d(p.getPosition()).add(
                    switch (rand.nextInt(2)) {
                        case 1 -> new Vector2d(
                            pickRandom(List.of(-DS_X, DS_X)), 
                            rand.nextInt(-DS_Y, DS_Y)
                        );
                        default -> new Vector2d(
                            rand.nextInt(-DS_X, DS_X), 
                            pickRandom(List.of(-DS_Y, DS_Y))
                        );
                    }
                );
                final Group g = fromProbability(rand.nextInt(PROBABILITY));
                return g.create(enemyFactory, origin, life);
            }).collect(Collectors.toSet()
        );
    }

}
