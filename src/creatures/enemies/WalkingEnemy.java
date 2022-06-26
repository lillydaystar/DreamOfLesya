package creatures.enemies;

import creatures.Creature;

abstract class WalkingEnemy extends Creature {

    private static final int GRAVITY = 2;
    private boolean onGround;

    WalkingEnemy(int x, int y) {
        super(x, y);
        this.velocityY = 0;
        this.velocityX = this.getHorizontalSpeed();
    }

    @Override
    public void update() {
        if (onGround) {
            velocityY = 2;
            ordinate -= velocityY;
        } else {
            velocityY += GRAVITY;
        }
        super.abscissa += super.velocityX;
        super.ordinate += velocityY;
        onGround = false;
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
//        super.collideVertically();
        this.onGround = true;
    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }
}