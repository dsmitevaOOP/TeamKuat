package sokoban;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Font;

import javax.swing.JPanel;

public class Board extends JPanel { 

    private final int OFFSET = 30;
    private final int SPACE = 20;
    private final int LEFT_COLLISION = 1;
    private final int RIGHT_COLLISION = 2;
    private final int TOP_COLLISION = 3;
    private final int BOTTOM_COLLISION = 4;
    
    private final int left = 1;
    private final int right = 2;
    private final int top = 3;  
    private final int bottom = 4;
    
    private final int undoNumber = 10;
    
    private boolean madeMove = false;
    
    private Vector<Integer> moveHistorySokoX = new Vector<Integer>();
    private Vector<Integer> moveHistorySokoY = new Vector<Integer>();
    private int undo = 0;
    
    private Vector<Integer> Direction = new Vector<Integer>();

    private ArrayList<Wall> walls = new ArrayList<Wall>();
    private ArrayList<Baggage> baggs = new ArrayList<Baggage>();
    private ArrayList<Area> areas = new ArrayList<Area>();
    private Player soko;
    private int w = 0;
    private int h = 0;
    private boolean completed = false;
    
    //1-st load welcome screen, lev=0 check for welcome screen
    private String level = levels.WelcomeToSocoban;
    int lev =0;
           

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        initWorld();
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    public final void initWorld() {
        
        int x = OFFSET;
        int y = OFFSET;
        
        Wall wall;
        Baggage b;
        Area a;


        for (int i = 0; i < level.length(); i++) {

            char item = level.charAt(i);

            if (item == '\n') {
                y += SPACE;
                if (this.w < x) {
                    this.w = x;
                }

                x = OFFSET;
            } else if (item == '#') {
                wall = new Wall(x, y);
                walls.add(wall);
                x += SPACE;
            } else if (item == '$') {
                b = new Baggage(x, y);
                baggs.add(b);
                x += SPACE;
            } else if (item == '.') {
                a = new Area(x, y);
                areas.add(a);
                x += SPACE;
            } else if (item == '@') {
                soko = new Player(x, y);
                x += SPACE;
            } else if (item == ' ') {
                x += SPACE;
            }

            h = y;
        }
    }

    public void buildWorld(Graphics g) {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        ArrayList<Actor> world = new ArrayList<Actor>();
        world.addAll(walls);
        world.addAll(areas);
        world.addAll(baggs);
        world.add(soko);

        for (int i = 0; i < world.size(); i++) {

            Actor item = (Actor) world.get(i);

            if ((item instanceof Player)
                    || (item instanceof Baggage)) {
                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
            } else {
                g.drawImage(item.getImage(), item.x(), item.y(), this);
            }

	//set welcomescreen
	 if (lev == 0) {
			g.setColor(new Color(250, 240, 170));
			g.setColor(new Color(0, 0, 0));
			g.setFont(new Font("default", Font.BOLD, 16));
			g.drawString("To move worker press arrow keys:", 150, 400);
			g.drawString("\u25B2 move up key, \u25BC move down key, \u25C4 move left key, \u25BA move right key",
						150, 420);
			g.drawString("To reset level press \"r\" key", 150, 440);
			g.drawString("To undo last move press \"u\" key", 150, 460);
			g.drawString("To continue press Enter", 150, 480);

			}	
            //if level is done, get next level and restart.
            if (completed) {
            	nextLevel(level);
                restartLevel();
                //g.setColor(new Color(0, 0, 0));
                //g.drawString("Completed", 25, 20);
            }

        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }

    class TAdapter extends KeyAdapter {


		@Override
        public void keyPressed(KeyEvent e) {

            if (completed) {
                return;
            }

            
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT) {
                if (checkWallCollision(soko,
                        LEFT_COLLISION)) {
                    return;
                }

                if (checkBagCollision(LEFT_COLLISION)) {
                    return;
                }

                soko.move(-SPACE, 0);
                madeMove = true;
                
                moveHistorySokoX.add(SPACE);
                moveHistorySokoY.add(0);
                Direction.add(left);

            } else if (key == KeyEvent.VK_RIGHT) {

                if (checkWallCollision(soko,
                        RIGHT_COLLISION)) {
                    return;
                }

                if (checkBagCollision(RIGHT_COLLISION)) {
                    return;
                }

                soko.move(SPACE, 0);
                madeMove = true;
                
                moveHistorySokoX.add(- SPACE);
                moveHistorySokoY.add(0);
                Direction.add(right);

            } else if (key == KeyEvent.VK_UP) {

                if (checkWallCollision(soko,
                        TOP_COLLISION)) {
                    return;
                }

                if (checkBagCollision(TOP_COLLISION)) {
                    return;
                }

                soko.move(0, -SPACE);
                madeMove = true;
                
                moveHistorySokoX.add(0);
                moveHistorySokoY.add(SPACE);
                Direction.add(top);

            } else if (key == KeyEvent.VK_DOWN) {

                if (checkWallCollision(soko,
                        BOTTOM_COLLISION)) {
                    return;
                }

                if (checkBagCollision(BOTTOM_COLLISION)) {
                    return;
                }

                soko.move(0, SPACE);
                madeMove = true;
                
                moveHistorySokoX.add(0);
                moveHistorySokoY.add(-SPACE);
                Direction.add(bottom);

            } else if (key == KeyEvent.VK_U) {
            	if (undo < undoNumber && madeMove) {
            		
            		madeMove = false;
            		undoLastMove();
                	undo++;
				}
            
            } else if (key == KeyEvent.VK_R) {
                
             
             restartLevel();
            }
            
            //choose a level
            switch (key) {
			case KeyEvent.VK_NUMPAD2:
				level = levels.level2;
				restartLevel();
				break;
			case KeyEvent.VK_2:
				level = levels.level2;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD3:
				level = levels.level3;
				restartLevel();
				break;
			case KeyEvent.VK_3:
				level = levels.level3;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD4:
				level = levels.level4;
				restartLevel();
				break;
			case KeyEvent.VK_4:
				level = levels.level4;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD5:
				level = levels.level5;
				restartLevel();
				break;
			case KeyEvent.VK_5:
				level = levels.level5;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD6:
				level = levels.level6;
				restartLevel();
				break;
			case KeyEvent.VK_6:
				level = levels.level6;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD7:
				level = levels.level7;
				restartLevel();
				break;
			case KeyEvent.VK_7:
				level = levels.level7;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD8:
				level = levels.level8;
				restartLevel();
				break;
			case KeyEvent.VK_8:
				level = levels.level8;
				restartLevel();
				break;
			case KeyEvent.VK_NUMPAD9:
				level = levels.level9;
				restartLevel();
				break;
			case KeyEvent.VK_9:
				level = levels.level9;
				restartLevel();
				break;
			//from start screen to level 1
			case KeyEvent.VK_ENTER:
				level = levels.level1;
				lev = 1;
				restartLevel();
				break;
			default:
				break;
			}

            repaint();
        }
    }
    
    public void undoLastMove()
  	{
  		if (moveHistorySokoX.size() > 0 && moveHistorySokoY.size() > 0 && Direction.size() > 0)
  		{
  			
  			if ((Direction.elementAt(Direction.size() - 1) == left)) {
  				
  				undoBaggMove(LEFT_COLLISION);
			}
  			
  			if ((Direction.elementAt(Direction.size() - 1) == right)) {
  				
  				undoBaggMove(RIGHT_COLLISION);
			}
  			
  			if ((Direction.elementAt(Direction.size() - 1) == top)) {
  				
  				undoBaggMove(TOP_COLLISION);
			}
  			
  			if ((Direction.elementAt(Direction.size() - 1) == bottom)) {
  				
  				undoBaggMove(BOTTOM_COLLISION);
			}
  				
  			soko.move(moveHistorySokoX.remove(moveHistorySokoX.size() - 1), 
  					moveHistorySokoY.remove(moveHistorySokoY.size() - 1));
  			
  		}
  	}

    private boolean checkWallCollision(Actor actor, int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isLeftCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isRightCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isTopCollision(wall)) {
                    return true;
                }
            }
            return false;

        } else if (type == BOTTOM_COLLISION) {

            for (int i = 0; i < walls.size(); i++) {
                Wall wall = (Wall) walls.get(i);
                if (actor.isBottomCollision(wall)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private boolean checkBagCollision(int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isLeftCollision(bag)) {

                    for (int j=0; j < baggs.size(); j++) {
                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isLeftCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                LEFT_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(-SPACE, 0);
                    
                    isCompleted();
                }
            }
            
            return false;

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isRightCollision(bag)) {
                    for (int j=0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isRightCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                RIGHT_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(SPACE, 0);
                    
                    isCompleted();                   
                }
            }
          
            return false;

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isTopCollision(bag)) {
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isTopCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                TOP_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(0, -SPACE);
                  
                    isCompleted();
                }
            }
            
            return false;

        } else if (type == BOTTOM_COLLISION) {
        
            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isBottomCollision(bag)) {
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        if (!bag.equals(item)) {
                            if (bag.isBottomCollision(item)) {
                                return true;
                            }
                        }
                        if (checkWallCollision(bag,
                                BOTTOM_COLLISION)) {
                            return true;
                        }
                    }
                    bag.move(0, SPACE);
                  
                    isCompleted();
                }
            }
        }
        
        return false;
    }
  


    private void undoBaggMove(int type) {

        if (type == LEFT_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isLeftCollision(bag)) {

                    for (int j=0; j < baggs.size(); j++) {
                        Baggage item = (Baggage) baggs.get(j);
                       
                        
                    }
                    bag.move(SPACE, 0);
                }
            }
            
            
            

        } else if (type == RIGHT_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
               if (soko.isRightCollision(bag)) {
                    for (int j=0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);    
                   }
                    bag.move(-SPACE, 0);                     
                }
            }
            
            

        } else if (type == TOP_COLLISION) {

            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isTopCollision(bag)) {
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        
                       
                    }
                    bag.move(0, SPACE);
                    
                    
                }
            }   
            

        } else if (type == BOTTOM_COLLISION) {
        
            for (int i = 0; i < baggs.size(); i++) {

                Baggage bag = (Baggage) baggs.get(i);
                if (soko.isBottomCollision(bag)) {
                    for (int j = 0; j < baggs.size(); j++) {

                        Baggage item = (Baggage) baggs.get(j);
                        
                    }
                    bag.move(0, -SPACE);
                    
                    
                }
            }
        }   
    }
   

    public void isCompleted() {

        int num = baggs.size();
        int compl = 0;

        for (int i = 0; i < num; i++) {
            Baggage bag = (Baggage) baggs.get(i);
            for (int j = 0; j < num; j++) {
                Area area = (Area) areas.get(j);
                if (bag.x() == area.x()
                        && bag.y() == area.y()) {
                    compl += 1;
                }
            }
        }

        if (compl == num) {
            completed = true;
            repaint();
        }
    }

    public void restartLevel() {

        areas.clear();
        baggs.clear();
        walls.clear();
        moveHistorySokoX = new Vector<Integer>();
        moveHistorySokoY = new Vector<Integer>();
        undo = 0;
        Direction = new Vector<Integer>();
        initWorld();
        if (completed) {
            completed = false;
        }
    }
    
    public String nextLevel(String s) {
		if (level == levels.level1) {
			level = levels.level2;
		} else if (level == levels.level2) {
			level = levels.level3;
		} else if (level == levels.level3) {
			level = levels.level4;
		} else if (level == levels.level4) {
			level = levels.level5;
		} else if (level == levels.level5) {
			level = levels.level6;
		} else if (level == levels.level6) {
			level = levels.level7;
		} else if (level == levels.level7) {
			level = levels.level8;
		} else if (level == levels.level8) {
			level = levels.level9;
		}
		return level;
	}

}
