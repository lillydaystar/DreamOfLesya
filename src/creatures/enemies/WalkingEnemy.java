package creatures.enemies;

import creatures.Creature;

abstract class WalkingEnemy extends Creature {

    private static final int GRAVITY = 2;
    private boolean onGround;
    private boolean alive = true;

    WalkingEnemy(int x, int y) {
        super(x, y);
        this.velocityY = 0;
        this.velocityX = this.getHorizontalSpeed();
    }

    @Override
    public void update() {
        if (alive) {
            if (this.onGround) {
                super.velocityY = GRAVITY;
            } else {
                velocityY += GRAVITY;
                super.ordinate += super.velocityY;
            }
            super.abscissa += super.velocityX;
            onGround = false;
        }
    }

    @Override
    public void leftCollision() {
        super.collideHorizontally();
    }

    @Override
    public void rightCollision() {
        super.collideHorizontally();
    }

    @Override
    public void upCollision() {
        super.collideVertically();
    }

    @Override
    public void downCollision() {
        this.onGround = true;
    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }
}