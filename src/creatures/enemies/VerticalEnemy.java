package creatures.enemies;

import creatures.Creature;

abstract class VerticalEnemy extends Creature {

    VerticalEnemy(int x, int y) {
        super(x, y);
        this.velocityX = 0;
        this.velocityY = this.getSpeed();
    }

    @Override
    public void update() {
//        if (this.upCollision || this.downCollision) {
//            this.collideHorizontally();
//        }
        super.ordinate += super.velocityY;
    }

    @Override
    public void leftCollision() {
        //do nothing; as creature moves vertically thus
        //horizontal collision will never occur
    }

    @Override
    public void rightCollision() {
        //do nothing; as creature moves vertically thus
        //horizontal collision will never occur
    }

    @Override
    public void upCollision() {
        super.collideVertically();
    }

    @Override
    public void downCollision() {
        super.collideVertically();
    }

    @Override
    protected void wake() {}
}