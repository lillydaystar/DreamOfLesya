package creatures.enemies;

import creatures.Creature;

abstract class BearingEnemy extends Creature {

    private int SCAN_RATE = 15;
    private int scan_counter = SCAN_RATE;

    BearingEnemy(int x, int y) {
        super(x, y);
    }

    @Override
    public void update() {
        if (scan_counter == SCAN_RATE) {
            count_velocity();
            scan_counter = -1;
        }
        this.abscissa += this.velocityX;
        this.ordinate += this.velocityY;
        scan_counter++;
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
        super.collideVertically();
    }

    @Override
    public void wake() {

    }

    private void count_velocity() {
        int distance_x = super.playerXmap - super.abscissa;
        int distance_y = super.playerYmap - super.ordinate;
        double distance = Math.sqrt(distance_x*distance_x + distance_y*distance_y);
        double sin_of_angle = distance_y/distance;
        double cos_of_angle = distance_x/distance;
        super.velocityX = (int)(getSpeed()*cos_of_angle);
        super.velocityY = (int)(getSpeed()*sin_of_angle);
    }
}