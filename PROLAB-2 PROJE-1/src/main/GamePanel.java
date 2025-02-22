package main;


import KısaYol.KısaYolBulma;
import Sandıklar.Sandıklar;
import entity.Player;
import tile.TileManager;
import entity.entity;
import javax.swing.*;
import java.awt.*;
import engeller.dinamikEngel;
import engeller.hareketliEngel;

public class GamePanel extends JPanel implements Runnable{

    // EKRAN AYARLARI

    final int originalTileSize = 8;
    final int scale = 3;

    public final int tileSize = originalTileSize * scale;//24x24

    public final int maxScreenCol =40;
    public final int maxScreenRow= 40;
    public final int screenWidth= tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    //DÜNYA AYARLARI
    public int maxWorldCol = harita.HaritaBoyutu;
    public int maxWorldRow = harita.HaritaBoyutu;
    public final int worldWidht = tileSize * maxWorldCol;
    public final int worldHeigt = tileSize * maxWorldRow;


    //FPS
    int FPS = 60;

    public TileManager tileM= new TileManager(this);

    public


    Thread gameThread;
    public ÇarpmaKontrol cKontrol = new ÇarpmaKontrol(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public kullaniciArayuz ui = new kullaniciArayuz(this);
    public Player player = new Player(this );

    public hareketliEngel npc[] = new hareketliEngel[20];
    public Sandıklar sandık[] = new Sandıklar[20];
    public dinamikEngel engel[] = new dinamikEngel[50];
    public KısaYolBulma yolBulma = new KısaYolBulma(this);

    public boolean[][] discovered = new boolean[maxWorldRow][maxWorldCol];; // Haritadaki her kare için keşfedildi mi bilgisini tutacak 2 boyutlu dizi
    public String[][] objects; // Haritadaki her kare için nesne bilgisini tutacak 2 boyutlu dizi


    public  GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);

    }

    public void setUpGame() {

        aSetter.setNPC();
        aSetter.setObject();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }



    @Override
    public void run() {
        double drawInterval =1000000000/ FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;
        while(gameThread != null){

            currentTime =System.nanoTime();
            timer =currentTime - lastTime;
            delta  += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;
            if(delta>1) {
                update();
                repaint();
                delta--;
                drawCount++;

            }

            if(timer >= 100000000){
                System.out.println("FPS; " + drawCount);
                drawCount= 0;
                timer = 0;
            }
        }
    }

    public void update(){

        player.update();

        for (int i = 0; i < npc.length; i++) {
            if (npc[i] != null){
                npc[i].update();
            }
        }

    }


    public void paintComponent(Graphics g) {

        super.paintComponent(g);


        Graphics2D g2 = (Graphics2D) g;

        //TILE
        tileM.draw(g2);


        //Player
        player.draw(g2);

        //NPC
        for (int i = 0; i < npc.length; i++) {
            if(npc[i] != null){
                npc[i].draw(g2);
            }
        }
        // Dinamik Engel
        for (int i = 0; i < engel.length; i++) {
            if(engel[i] !=  null){
                engel[i].draw(g2,this);
            }
        }

        //Sandıklar
        for(int i = 0; i < sandık.length ;i++){
            if(sandık[i] != null){
                sandık[i].draw(g2,this);
            }
        }



        //Kullanıcı Arayüz
        ui.draw(g2);

//
//        SİS YAPMA VE GÖSTERME

//        for (int i = 0; i < maxWorldRow; i++) {
//            for (int j = 0; j < maxWorldCol; j++) {
//                if (!discovered[i][j]) {
//                    g2.setColor(Color.GRAY);
//                    g2.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
//                }
//            }
//        }
//
//        // discovered ve objects dizilerini güncelle
//        updateDiscovered();
//        updateObjects();

        g2.dispose();

    } public void updateDiscovered() {
        int tileCol = player.worldx / tileSize;
        int tileRow = player.worldy / tileSize;

        // Karakterin bulunduğu kareyi ve çevresindeki kareleri keşfet
        for (int i = tileCol- 1; i <= tileCol+ 1; i++) {
            for (int j = tileRow - 1; j <= tileRow + 1; j++) {
                // Eğer kare harita sınırları içerisindeyse keşfedildi olarak işaretle
                if (i >= 0 && i < maxScreenCol && j >= 0 && j < maxScreenRow) {
                    discovered[i][j] = true;
                }
            }
        }
    }

    // objects dizisini güncelle (karelerdeki nesneleri ekleyin)
    public void updateObjects() {
        // Nesneleri eklemek için gerekli kodu buraya ekleyin
        // Örneğin:
        // objects[i][j] = "Ağaç";
        // objects[i][j] = "Gümüş sandık";
    }
}
