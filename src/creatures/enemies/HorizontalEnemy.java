package creatures.enemies;

import creatures.Creature;

abstract class HorizontalEnemy extends Creature {

//    private boolean leftCollision, rightCollision;

    HorizontalEnemy(int x, int y) {
        super(x, y);
        this.velocityX = this.getHorizontalSpeed();
        this.velocityY = 0;
    }

    @Override
    public void update() {
//        if (this.rightCollision || this.leftCollision)
//            this.collideHorizontally();
//        this.leftCollision = this.rightCollision = true;
        super.abscissa += super.velocityX;
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
        //do nothing; as creature moves horizontally thus
        //vertical collision will never occur
    }

    @Override
    public void downCollision() {
        //do nothing; as creature moves horizontally thus
        //vertical collision will never occur
    }

    @Override
    protected int getVerticalSpeed() {
        return 0;
    }
}