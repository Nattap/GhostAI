import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import commandcenter.CommandCenter;
import gameInterface.AIInterface;
import structs.FrameData;
import structs.GameData;
import structs.Key;

public class Indiv_GhostReader implements AIInterface {

	boolean isplayer1;
	GameData gdata;
	public static ArrayList<ArrayList<Integer>> Act = new ArrayList<ArrayList<Integer>>();
	CommandCenter cc;
	FrameData fd;
	Key input;
	// public static File ghostfile;
	public static BufferedReader reader;
	public static PrintWriter writer;
	public static InputStream in;
	public static final int TOTALCASE = 90720;
	public static final int[] ACTIONLIST = { 0, 0, 32, 35, 36, 0, 38, 37, 39, 0, 33, 34, 33, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 1, 2, 0, 0, 3, 4, 5, 6, 18, 19, 20, 21, 7, 8, 9, 10, 22, 23, 24, 25, 11, 12, 13, 14, 15, 16, 17, 26, 27,
			28, 29, 30, 31, 13 };

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return CHARACTER_ZEN;
	}

	@Override
	public void getInformation(FrameData arg0) {
		// TODO Auto-generated method stub
		fd = arg0;
		cc.setFrameData(fd, isplayer1);
	}

	@Override
	public int initialize(GameData arg0, boolean arg1) {
		// System.out.println(ACTIONLIST[55]);
		isplayer1 = arg1;
		gdata = arg0;
		input = new Key();
		fd = new FrameData();
		cc = new CommandCenter();
		// ghostfile = new File("./ghostdata/ghost.csv");
		in = getClass().getResourceAsStream("/ghost.csv");
		try {
			/*
			 * if(ghostfile.createNewFile()){ writer = new PrintWriter(new
			 * BufferedWriter(new FileWriter(ghostfile))); for(int
			 * i=0;i<262144;i++){ writer.println(63); } writer.close(); }
			 */
			reader = new BufferedReader(new InputStreamReader(in));
			for (int i = 0; i < TOTALCASE; i++) {
				Act.add(new ArrayList<Integer>());
				// System.out.println("x");
			}
			// System.out.println(1);
			// reader.readLine();
			// System.out.println(2);
			for (int i = 0; i < TOTALCASE; i++) {
				String x = reader.readLine();
				String[] tokens = x.split(",");
				// System.out.println("read");
				for (String token : tokens) {
					if (token == "")
						continue;
					if (Integer.parseInt(token) == 63)
						break;
					Act.get(i).add(Integer.parseInt(token));
					// System.out.println("added");
				}
			}
			reader.close();
			// ghostfile.delete();
		} catch (Exception e) {
			// TODO: handle exception
			try {
				reader.close();
				writer.close();
			} catch (Exception ee) {
				// TODO: handle exception
				e.printStackTrace();
			}
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Key input() {
		// TODO Auto-generated method stub
		return input;
	}

	@Override
	public void processing() {
		if (!fd.getEmptyFlag() && fd.getRemainingTimeMilliseconds() > 0) {
			if (cc.getSkillFlag()) {
				input = cc.getSkillKey();
				return;
			}
			// System.out.println(cc.getMyCharacter().getAction().ordinal());
			// System.out.println(cc.getEnemyX());
			int deltax = cc.getDistanceX();
			deltax = DxtoCode(deltax);
			int deltay = DytoCode((cc.getMyCharacter().getTop() + cc.getMyCharacter().getBottom()) / 2,
					(cc.getEnemyCharacter().getTop() + cc.getEnemyCharacter().getBottom()) / 2);
			// boolean playerair = cc.getMyCharacter().getTop() +
			// cc.getMyCharacter().getBottom() < 500;
			// boolean enemyair = cc.getEnemyCharacter().getTop() +
			// cc.getEnemyCharacter().getBottom() < 500;
			int enemyact = cc.getEnemyCharacter().getAction().ordinal();
			int projectile;
			if (isplayer1) {
				projectile = fd.getProjectilesByP2().isEmpty() ? 0 : 1;
			} else {
				projectile = fd.getProjectilesByP1().isEmpty() ? 0 : 1;
			}
			// System.out.println("deltax before = "+deltax);
			int index = Encrypt(enemyact, deltax, deltay, EtoCode(cc.getMyEnergy()), EtoCode(cc.getEnemyEnergy()),
					projectile, Checkcorner(cc.getMyX(), cc.getEnemyX()),
					enemystate(cc.getEnemyCharacter().getAction().ordinal()));
			// int index = enemyact * 1024 + deltax * 4 + (playerair ? 2 : 0) +
			// (enemyair ? 1 : 0);
			// System.out.println(index);
			// index = 4328;
			input.empty();
			int reaction = React(index);
			// System.out.println(reaction);
			switch (reaction) {
			case (0):
				break;
			case (1):
				if (enoughmana(5))
					cc.commandCall("THROW_A");
				else
					moveforward();
				break;
			case (2):
				if (enoughmana(20))
					cc.commandCall("THROW_B");
				else
					moveforward();
				break;
			case (3):
				cc.commandCall("STAND_A");
				break;
			case (4):
				cc.commandCall("STAND_B");
				break;
			case (5):
				cc.commandCall("CROUCH_A");
				break;
			case (6):
				cc.commandCall("CROUCH_B");
				break;
			case (7):
				cc.commandCall("STAND_FA");
				break;
			case (8):
				cc.commandCall("STAND_FB");
				break;
			case (9):
				cc.commandCall("CROUCH_FA");
				break;
			case (10):
				cc.commandCall("CROUCH_FB");
				break;
			case (11):
				if (enoughmana(2))
					cc.commandCall("STAND_D_DF_FA");
				else
					moveforward();
				break;
			case (12):
				if (enoughmana(30))
					cc.commandCall("STAND_D_DF_FB");
				else
					moveforward();
				break;
			case (13):
				if (enoughmana(150))
					cc.commandCall("STAND_D_DF_FC");
				else
					moveforward();
				break;
			case (14):
				cc.commandCall("STAND_F_D_DFA");
				break;
			case (15):
				if (enoughmana(50))
					cc.commandCall("STAND_F_D_DFB");
				else
					moveforward();
				break;
			case (16):
				cc.commandCall("STAND_D_DB_BA");
				break;
			case (17):
				if (enoughmana(50))
					cc.commandCall("STAND_D_DB_BB");
				else
					moveforward();
				break;
			case (18):
				cc.commandCall("AIR_A");
				break;
			case (19):
				cc.commandCall("AIR _B");
				break;
			case (20):
				if (enoughmana(5))
					cc.commandCall("AIR _DA");
				else
					moveforward();
				break;
			case (21):
				if (enoughmana(5))
					cc.commandCall("AIR _DB");
				else
					moveforward();
				break;
			case (22):
				cc.commandCall("AIR _FA");
				break;
			case (23):
				cc.commandCall("AIR _FB");
				break;
			case (24):
				cc.commandCall("AIR _UA");
				break;
			case (25):
				cc.commandCall("AIR _UB");
				break;
			case (26):
				cc.commandCall("AIR _D_DF_FA");
				break;
			case (27):
				if (enoughmana(50))
					cc.commandCall("AIR _D_DF_FB");
				else
					moveforward();
				break;
			case (28):
				if (enoughmana(10))
					cc.commandCall("AIR _F_D_DFA");
				else
					moveforward();
				break;
			case (29):
				if (enoughmana(40))
					cc.commandCall("AIR _F_D_DFB");
				else
					moveforward();
				break;
			case (30):
				if (enoughmana(10))
					cc.commandCall("AIR _D_DB_BA");
				else
					moveforward();
				break;
			case (31):
				if (enoughmana(50))
					cc.commandCall("AIR _D_DB_BB");
				else
					moveforward();
				break;
			case (32):
				if (cc.getMyCharacter().front) {
					input.R = true;
				} else {
					input.L = true;
				}
				break;
			case (33):
				if (!cc.getMyCharacter().front) {
					input.R = true;
				} else {
					input.L = true;
				}
				break;
			case (34):
				input.D = true;
				if (!cc.getMyCharacter().front) {
					input.R = true;
				} else {
					input.L = true;
				}
				break;
			case (35):
				cc.commandCall("DASH");
				break;
			case (36):
				cc.commandCall("BACK_STEP");
				break;
			case (37):
				input.U = true;
				if (cc.getMyCharacter().front) {
					input.R = true;
				} else {
					input.L = true;
				}
				break;
			case (38):
				input.U = true;
				break;
			case (39):
				input.U = true;
				if (!cc.getMyCharacter().front) {
					input.R = true;
				} else {
					input.L = true;
				}
				break;
			default:
				moveforward();
				break;
			}
		}
	}

	boolean enoughmana(int a) {
		return cc.getMyCharacter().getEnergy() >= a;
	}

	void moveforward() {
		if (cc.getMyCharacter().front) {
			input.R = true;
		} else {
			input.L = true;
		}
		return;
	}

	public int Encrypt(int actionid, int deltax, int deltay, int penergy, int eenergy, int projectile, int corner,
			int enemystate) {
		return projectile + eenergy * 2 + penergy * 2 * 3 + deltay * 2 * 9 + deltax * 18 * 16 + corner * 18 * 16 * 21
				+ enemystate * 18 * 16 * 21
						* 3 /* + actionid * 18 * 16 * 21 * 3 * 5 */;
	}

	public int enemystate(int enemyaction) {
		if (enemyaction <= 9) {
			return 0;
		}
		if (enemyaction <= 12) {
			return 2;
		}
		if (enemyaction <= 18) {
			return 3;
		}
		if (enemyaction <= 22 || enemyaction == 26) {
			return 4;
		}
		return 1;
	}

	public int Checkcorner(int selfx, int enemyx) {
		if (selfx < 0) {
			if (enemyx > selfx) {
				return 1;
			} else {
				return 2;
			}
		}
		if (selfx > 560) {
			if (enemyx < selfx) {
				return 1;
			} else {
				return 2;
			}
		}
		if (enemyx < 0 || enemyx > 580) {
			return 2;
		}
		return 0;
	}

	public int EtoCode(int energy) {
		if (energy < 20) {
			return 0;
		}
		if (energy < 150) {
			return 1;
		}
		return 2;
	}

	public int DxtoCode(int dx) {
		if (dx > 200) {
			return 20;
		} else
			return dx / 10;
	}

	public int DytoCode(int py, int ey) {
		int ans = 0;
		if (py > 0) {
			if (py > 400) {
				py = 399;
			}
			ans += (py / 100) * 4;
		}
		if (ey > 0) {
			if (ey > 400) {
				ey = 399;
			}
			ans += (ey / 100);
		}
		return ans;
	}

	public int React(int index) {
		// Find shifting
		int dx, dy, aid, pp, pe, pro;
		dx = (index % 6048) / 288;
		dy = (index % 288) / 18;
		int pdy = dy / 4;
		int edy = dy % 4;
		aid = index / 6048;
		pp = (index % 18) / 6;
		pe = (index % 6) / 2;
		pro = index % 2;
		int deltaxshift = 0;
		// int actionshift = 0;
		int penergyshift = 0;
		int eenergyshift = 0;
		int pdeltayshift = 0;
		int edeltayshift = 0;
		int projectiles = 0;
		// if(Act.get(index).size()==0)System.out.println("Shifted");
		// else System.out.println("Not Shifted");
		// System.out.println("deltax = " + dx);
		while (Act.get(index).size() == 0) {
			if (deltaxshift <= 0) {
				deltaxshift = -deltaxshift;
				deltaxshift++;
			} else {
				deltaxshift = -deltaxshift;
			}
			if (deltaxshift == 3) {
				deltaxshift = 0;
				eenergyshift++;
			}
			if (eenergyshift == 3) {
				eenergyshift = 0;
				penergyshift++;
			}
			if (penergyshift == 3) {
				penergyshift = 0;
				switch (pdeltayshift) {
				case (0):
					pdeltayshift = 1;
					break;
				case (1):
					pdeltayshift = -1;
					break;
				case (-1):
					pdeltayshift = 2;
					break;
				}
			}
			if (pdeltayshift == 2) {
				pdeltayshift = 0;
				switch (edeltayshift) {
				case (0):
					edeltayshift = 1;
					break;
				case (1):
					edeltayshift = -1;
					break;
				case (-1):
					edeltayshift = 2;
					break;
				}
			}
			if (edeltayshift == 2) {
				edeltayshift = 0;
				projectiles++;
			}
			if (projectiles == 2) {
				return 0;
			}
			if (dx + deltaxshift < 0 || dx + deltaxshift > 20)
				continue;
			if (pdy + pdeltayshift < 0 || pdy + pdeltayshift > 3)
				continue;
			if (edy + edeltayshift < 0 || edy + edeltayshift > 3)
				continue;
			index = Encrypt(0, dx + deltaxshift, (pdy + pdeltayshift) * 4 + edy + edeltayshift, penergyshift,
					eenergyshift, projectiles == pro ? 0 : 1, aid, 0);
		}
		if (deltaxshift != 0) {
			// System.out.println("Shifted by " + deltaxshift);
		}
		System.out.println("index = " + index);
		Random rand = new Random();
		int react = 0;
		if (Act.get(index).size() > 1) {
			react = rand.nextInt(Act.get(index).size() - 1);
		}
		int reaction;
		if (Act.get(index).size() > 0) {
			reaction = Act.get(index).get(react);
		} else {
			reaction = 0;
		}
		return reaction;
	}
}
