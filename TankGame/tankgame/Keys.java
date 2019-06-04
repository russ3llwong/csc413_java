package tankgame;

class Keys {
    
    private int forwardKey, rightKey, backwardKey, leftKey, shootKey;
    
    Keys(int forward , int right, int backward, int left, int shoot){
       this.forwardKey = forward;
       this.rightKey = right;
       this.backwardKey = backward;
       this.leftKey = left;
       this.shootKey = shoot;    
    }

    int getForwardKey(){
        return this.forwardKey;
    }

    int getRightKey(){
        return this.rightKey;
    }

    int getBackwardKey(){
        return this.backwardKey;
    }

    int getLeftKey(){
        return this.leftKey;
    }

    int getShootKey(){
        return this.shootKey;
    }    
}
