package logic;

import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

//blocks are all those tiles that you can walk on and collide against
//they do not entail any kind of interaction
public class Block extends Tile {

    public Block(String imgName,int i, int j) {
        super(i,j);
        this.imgName=imgName;
        loadInformations();
    }

    @Override
    protected void initializeStuff() {
        x=col*TILE_SIZE;
        y=row*TILE_SIZE;
        boundingBox=new Rectangle(x,y,TILE_SIZE,TILE_SIZE);
    }

    protected void loadInformations() {
        try {
            image=ImageIO.read(getClass().getResource("/images/"+imgName+".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String imgName;
    private int x;
    private int y;
}