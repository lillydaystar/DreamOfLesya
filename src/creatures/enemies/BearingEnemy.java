package creatures.enemies;

import creatures.Creature;
import creatures.CreatureState;
import graphics.GameWindow;

abstract class BearingEnemy extends Creature {

    private int SCAN_RATE = 15;
    private int scan_counter = SCAN_RATE;

    BearingEnemy(int x, int y) {
        super(x, y);
        this.state = CreatureState.Wait;
    }

    @Override
    public void update() {
        if (this.state == CreatureState.Alive) {
            if (scan_counter == SCAN_RATE) {
                count_velocity();
                scan_counter = -1;
            }
            this.abscissa += this.velocityX;
            this.ordinate += this.velocityY;
            scan_counter++;
//            random_velocity();
        }
    }

    @Override
    public void leftCollision() {
//        super.left_collision = true;
        super.collideHorizontally();
    }

    @Override
    public void rightCollision() {
//        super.right_collision = true;
        super.collideHorizontally();
    }

    @Override
    public void upCollision() {
//        super.top_collision = true;
        super.collideVertically();
    }

    @Override
    public void downCollision() {
//        super.bottom_collision = true;
        super.collideVertically();
    }

    @Override
    public void wake() {
        if (this.state == CreatureState.Wait) {
            this.state = CreatureState.Alive;
        }
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

    private void random_velocity() {
        if (top_collision || bottom_collision
               || right_collision || left_collision) {
            double cos_new_angle = super.random.nextDouble();
            double sin_new_angle = Math.sqrt(1 - cos_new_angle*cos_new_angle);
            boolean direction = super.random.nextBoolean();
            if (top_collision && bottom_collision) {
                super.velocityY = 0;
                if (right_collision && left_collision)
                    super.velocityX = 0;
                else if (right_collision)
                    super.velocityX = -getSpeed();
                else if (left_collision)
                    super.velocityX = getSpeed();
                else
                    super.velocityX = getSpeed() * (direction ? -1 : 1);
            } else if (top_collision) {
                if (right_collision && left_collision) {
                    super.velocityY = getSpeed();
                    super.velocityX = 0;
                } else if (right_collision) {
                    super.velocityX = -(int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                    System.out.println(velocityX+" "+velocityY);
                } else if (left_collision) {
                    super.velocityX = (int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                } else {
                    super.velocityX = getSpeed() * (direction ? 1 : -1);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                }
            } else if (bottom_collision) {
                if (right_collision && left_collision) {
                    super.velocityY = -getSpeed();
                    super.velocityX = 0;
                } else if (right_collision) {
                    super.velocityX = -(int)(getSpeed()*cos_new_angle);
                    super.velocityY = -(int)(getSpeed()*sin_new_angle);
                } else if (left_collision) {
                    super.velocityX = (int)(getSpeed()*cos_new_angle);
                    super.velocityY = -(int)(getSpeed()*sin_new_angle);
                } else {
                    super.velocityX = getSpeed() * (direction ? 1 : -1);
                    super.velocityY = -(int)(getSpeed()*sin_new_angle);
                }
            } else {
                if (right_collision && left_collision) {
                    super.velocityX = 0;
                    super.velocityY = getSpeed() * (direction ? 1 : -1);
                } else if (right_collision) {
                    super.velocityX = -(int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle) * (direction ? 1 : -1);
                } else if (left_collision) {
                    super.velocityX = (int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle) * (direction ? 1 : -1);
                }
            }
        }
        top_collision = bottom_collision = right_collision = left_collision = false;
    }
}