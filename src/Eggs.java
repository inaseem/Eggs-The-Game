
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Tony Stark
 */
public class Eggs extends JPanel implements Runnable{

    Thread th;
    Graphics2D g;
    int dx,dy;
    Bowl b= new Bowl();
    int NUM=5;
    Egg egg[]= new Egg[NUM];
    EggMover movers[]= new EggMover[NUM];
    boolean inside=false;
    Timer timer[] = new Timer[NUM];
    static int FWIDTH=500,FHEIGHT=400;
    int in=0;
    int speed=10;
    int lifes=10;
    static Color c1=Color.ORANGE,c2=Color.YELLOW;
    //static Color c1=Color.decode("#689F38"),c2=Color.decode("#C8E6C9");
    
    public Eggs(){
        
        b.setMx(30);
        b.setMy(200);
        setOpaque(false);
        try{
            setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("Tahoma", 1, 24), new Color(204, 0, 0)));
            for(LookAndFeelInfo inf: UIManager.getInstalledLookAndFeels()){
                if(inf.getName().equals("Nimbus")){
                    UIManager.setLookAndFeel(inf.getClassName());
                    break;
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        //Listener Starts//
       
        for(int i=0;i<egg.length;++i){
            egg[i]= new Egg();
            egg[i].setMy(30);
            egg[i].reset();
        }
        
        for(in=0;in<egg.length;++in){
            movers[in]= new EggMover(egg[in],b);
            movers[in].setInitialDelay((in+1)*1500);
            movers[in].move();
         
        }
        MouseListener ml = new MouseAdapter(){
            public void mousePressed(MouseEvent me){
                dx=(int)me.getPoint().getX();
                if(b.contains(me.getPoint())){
                inside=true;}
                
            }
            public void mouseReleased(MouseEvent me){
                inside=false;
            }
        };
        addMouseListener(ml);
        
        MouseMotionListener mll = new MouseAdapter(){
            public void mouseDragged(MouseEvent me){
                if(inside==true){
                b.setMx((int)me.getPoint().getX());
                }
            }
        };
        addMouseMotionListener(mll);
        
        
        th= new Thread(this);
        th.start();
    }
    
    public void paint(Graphics g2){
    g=(Graphics2D)g2;
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    super.paint(g);
    GradientPaint gpp = new GradientPaint(getWidth()/2,0,c1,getWidth()/2,getHeight(),c2);
    g.setPaint(gpp);
    g.fillRect(0, 0, getWidth(), getHeight());
    b.setMy(getHeight()-35);
    Polygon poly = new Polygon();
    poly.addPoint((getWidth()/3)/2,getHeight()-40);
    poly.addPoint(getWidth()-(getWidth()/3)/2, getHeight()-40);
    poly.addPoint(getWidth()-10, getHeight()-10);
    poly.addPoint(10, getHeight()-10);
    GradientPaint gp = new GradientPaint(getWidth()/2,getHeight(),Color.WHITE,getWidth()/2,getHeight()-40,Color.DARK_GRAY);
    g.setPaint(gp);
    g.fill(poly);
    b.drawOn(g);    
    for(int i=0;i<egg.length;++i){
                egg[i].drawOn(g);
    }
    }
    
    
    
    
    public void run(){
        try{
            while(true){
                th.sleep(speed);
                if(b.lifes==0){
                    for(int i=0;i<egg.length;++i){
                                egg[i].reset();
                                movers[i].stop();
                            }
                            JOptionPane.showMessageDialog(null, "Your Score Was "+b.score);
                            int a=JOptionPane.showConfirmDialog(null, "Do You Want To Restart The Game", "Game Over", JOptionPane.OK_CANCEL_OPTION);
                            
                        if(a==JOptionPane.OK_OPTION){
                            for(int i=0;i<egg.length;++i){
                               movers[i].setInitialDelay((i+1)*1500);
                                movers[i].move();
                            }
                            b.score=-10;
                            b.lifes=10;
                            b.updateScore();
                            
                        }
                        if(a==JOptionPane.CANCEL_OPTION){
                            System.exit(0);
                        }
                        }
                repaint();
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static void main(String args[]){
    
        JFrame jfm = new JFrame();
        jfm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jfm.setLayout(new BorderLayout());
        jfm.getContentPane().setBackground(c1);
        JLabel l= new JLabel("Your Score Along With Life Is Displayed Here");
        l.setBorder(BorderFactory.createTitledBorder(null, "Eggs!!", TitledBorder.CENTER, TitledBorder.ABOVE_TOP, new Font("Tahoma", 1, 24), Color.WHITE));
        jfm.add(l,BorderLayout.NORTH);
        final Eggs eg = new Eggs();
        eg.b.setOutputComponent(l);
        KeyListener kl = new KeyAdapter(){
            public void keyPressed(KeyEvent ke){
          if(ke.getKeyCode()==KeyEvent.VK_RIGHT){
              eg.b.setMx(eg.b.MX+2);
          }
          if(ke.getKeyCode()==KeyEvent.VK_LEFT){
              eg.b.setMx(eg.b.MX-2);
          }
            }
        };
        jfm.addKeyListener(kl);
        jfm.add(eg);
        jfm.setSize(FWIDTH, FHEIGHT);
        jfm.setVisible(true);
    }
}

class EggMover{
    
    ActionListener al;
    Timer timer;
    Egg egg;
    Bowl b;
    boolean allowed=true;
    
    public EggMover(Egg eg,Bowl bl){
    egg=eg;
    b=bl;
    al=new ActionListener(){
        public void actionPerformed(ActionEvent ae){
            egg.setMy(egg.MY+1);
            if(isAllowed()){
                if(egg.fallsInBowl(b)){
                    egg.reset();
                    b.updateScore();
                }
            }
                if(egg.r.y+egg.r.height>b.r.y+b.r.height/2&&!egg.fallsInBowl(b)){
                    allowed=false;
                    if(egg.r.y+egg.r.height*2>=275){
                        egg.reset();
                        b.lifes-=1;
                        b.score-=10;
                        b.updateLife();
                    }
                }
            
                else{
                    allowed=true;
               }
        }
    };
    timer= new Timer(10,al);
    }
    void setInitialDelay(int i){
        timer.setInitialDelay(i);
    }
    
    boolean isAllowed(){
        return allowed;
    }
    
    void move(){
        timer.start();
    }
    
    void stop(){
        timer.stop();
    }
}

class Egg{

    int MX,MY;
    Rectangle r;
    Random rn= new Random();
    
    Egg(){
        
    }
    
    boolean fallsInBowl(Bowl b){
        return r.intersects(b.r);
    }
    
    void setMx(int dx){
       MX=dx; 
    }
    
    void setMy(int dy){
        MY=dy;
    }
    
    public void reset(){
        setMy(30);
        setMx(30+rn.nextInt(Eggs.FWIDTH-40));
    }
    
    void drawOn(Graphics2D g){
        GradientPaint gp= new GradientPaint(MX,MY,Color.GRAY.brighter(),MX+5,MY+15/2,Color.lightGray.brighter());
        g.setPaint(gp);
        g.fillOval(MX, MY, 10, 15);
        r=new Rectangle(MX,MY,10,15/2);
    }
}

class Bowl{

    int MX,MY;
    Rectangle r;
    JLabel l;
    int score=0,lifes=10;
    Bowl(){
    }
    
    void setMx(int dx){
        MX=dx;
    }
    
    void setOutputComponent(JLabel lb){
        l=lb;
    }
    
    void updateScore(){
      l.setText("Score = "+ (score+=10)+"          Lifes = "+(lifes));
    }
    
    void updateLife(){
        l.setText("Score = "+ (score+=10)+"          Lifes = "+(lifes));
    }
    
    void setMy(int dy){
        MY=dy;
    }
    
    boolean contains(Point p){
        return r.contains(p);
    }
    
    void drawOn(Graphics2D g){
        GradientPaint gp1= new GradientPaint(MX-15,MY-10,Color.LIGHT_GRAY,MX-15+30/2,MY-10+30/2,Color.WHITE,true);
        g.setPaint(gp1);
        g.fillArc(MX-15, MY-10, 30, 30, 0, -180);
        GradientPaint gp2= new GradientPaint(MX,MY,Color.WHITE,MX,MY+10,Color.DARK_GRAY.brighter());
        g.setPaint(gp2);
        g.fillOval(MX-15, MY, 30, 10);
        r=new Rectangle(MX-15,MY,30,20);
    }
}
