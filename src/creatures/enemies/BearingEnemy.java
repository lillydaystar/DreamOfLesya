package creatures.enemies;

import creatures.Creature;
import creatures.CreatureState;

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
                if (right_collision && left_collision) {
                    System.out.println("both both");
                    super.velocityX = 0;
                } else if (right_collision) {
                    super.velocityX = -getSpeed();
                    System.out.println("both r");
                } else if (left_collision) {
                    super.velocityX = getSpeed();
                    System.out.println("both l");
                } else {
                    super.velocityX = getSpeed() * (direction ? -1 : 1);
                    System.out.println("both n");
                }
            } else if (top_collision) {
                if (right_collision && left_collision) {
                    super.velocityX = 0;
                    super.velocityY = getSpeed();
                    System.out.println("top both");
                } else if (right_collision) {
                    super.velocityX = -(int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                    System.out.println("top r");
                } else if (left_collision) {
                    super.velocityX = (int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                    System.out.println("top l");
                } else {
                    super.velocityX = (int)(getSpeed()*cos_new_angle) * (direction ? 1 : -1);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                    System.out.println("top n");
                }
            } else if (bottom_collision) {
                if (right_collision && left_collision) {
                    super.velocityX = 0;
                    super.velocityY = -getSpeed();
                    System.out.println("bot both");
                } else if (right_collision) {
                    super.velocityX = -(int)(getSpeed()*cos_new_angle);
                    super.velocityY = -(int)(getSpeed()*sin_new_angle);
                    System.out.println("bot r");
                } else if (left_collision) {
                    super.velocityX = (int)(getSpeed()*cos_new_angle);
                    super.velocityY = -(int)(getSpeed()*sin_new_angle);
                    System.out.println("bot l");
                } else {
                    super.velocityX = (int)(getSpeed()*cos_new_angle) * (direction ? 1 : -1);
                    super.velocityY = -(int)(getSpeed()*sin_new_angle);
                    System.out.println("bot n");
                }
            } else {
                if (right_collision && left_collision) {
                    super.velocityX = 0;
                    super.velocityY = getSpeed() * (direction ? 1 : -1);
                    System.out.println("n both");
                } else if (right_collision) {
                    super.velocityX = -(int)(getSpeed()*cos_new_angle);
                    super.velocityY = (int)(getSpeed()*sin_new_angle) * (direction ? 1 : -1);
                    System.out.println("n r");
                } else if (left_collision) {
                    super.velocityX = (int)(getSpeed()*cos_new_angle) * (direction ? 1 : -1);
                    super.velocityY = (int)(getSpeed()*sin_new_angle);
                    System.out.println("n l");
                }
            }
        }
        System.out.println(velocityX+" "+velocityY);
        top_collision = bottom_collision = right_collision = left_collision = false;
    }
}