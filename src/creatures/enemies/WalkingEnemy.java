package creatures.enemies;

import creatures.Creature;
import creatures.CreatureState;
import graphics.GameWindow;

abstract class WalkingEnemy extends Creature {

    private static final int GRAVITY = 2;
    private boolean onGround;

    WalkingEnemy(int x, int y) {
        super(x, y);
        this.velocityY = 0;
        this.velocityX = -this.getHorizontalSpeed();
        super.state = CreatureState.Wait;
    }

    @Override
    public void update() {
        if (super.state == CreatureState.Alive) {
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
        this.ordinate = ((this.ordinate + this.velocityY)/GameWindow.blockSize)*GameWindow.blockSize;
    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }

    @Override
    protected void wake() {
        if (this.state == CreatureState.Wait) {
            this.state = CreatureState.Alive;
            this.velocityY = GRAVITY;
        }
    }
}