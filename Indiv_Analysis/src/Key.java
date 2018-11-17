
/**
 * The class which stores the key corresponding to the action.<br>
 * Key is represented as follows.
 *
 * <ul>
 * <li><strong>A</strong> {@literal ->} P1: Z, P2: T</li>
 * <li><strong>B</strong> {@literal ->} P1: X, P2: Y</li>
 * <li><strong>C</strong> {@literal ->} P1: C, P2: U</li>
 * <li><strong>U</strong> {@literal ->} P1: Up arrow, P2: I</li>
 * <li><strong>R</strong> {@literal ->} P1: Right arrow, P2: L</li>
 * <li><strong>D</strong> {@literal ->} P1: Down arrow, P2: K</li>
 * <li><strong>L</strong> {@literal ->} P1: Left arrow, P2: J</li>
 * </ul>
 */
public class Key {

	/**
	 * If the value is set to true, then the "A" button will be pressed.
	 */
	public boolean A;

	/**
	 * If the value is set to true, then the "B" button will be pressed.
	 */
	public boolean B;

	/**
	 * If the value is set to true, then the "C" button will be pressed.
	 */
	public boolean C;

	/**
	 * If the value is set to true, then the "Up" button will be pressed.
	 */
	public boolean U;

	/**
	 * If the value is set to true, then the "Right" button will be pressed.
	 */
	public boolean R;

	/**
	 * If the value is set to true, then the "Down" button will be pressed.
	 */
	public boolean D;

	/**
	 * If the value is set to true, then the "Left" button will be pressed.
	 */
	public boolean L;

	/**
	 * This constructor initializes all keys to false, or not pressed.
	 */
	public Key() {
		this.empty();
	}

	/**
	 * This is the copy constructor of the Key class.
	 *
	 * @param key
	 *            An object key
	 */
	public Key(Key key) {
		this.A = key.A;
		this.B = key.B;
		this.C = key.C;
		this.U = key.U;
		this.R = key.R;
		this.D = key.D;
		this.L = key.L;
	}

	/**
	 * Resets all keys to false, or not pressed.
	 */
	public void empty() {
		this.A = false;
		this.B = false;
		this.C = false;
		this.U = false;
		this.R = false;
		this.D = false;
		this.L = false;
	}

	/**
	 * Converts the key represented in byte to the key represented in boolean
	 *
	 * @param keyByte
	 *            The key represented in byte
	 *
	 * @return The key represented in boolean
	 */
	public Key convertByteToKey(byte keyByte) {
		Key tempKey = new Key();

		tempKey.U = byteToBinary(keyByte / 64);
		keyByte %= 64;
		tempKey.R = byteToBinary(keyByte / 32);
		keyByte %= 32;
		tempKey.L = byteToBinary(keyByte / 16);
		keyByte %= 16;
		tempKey.D = byteToBinary(keyByte / 8);
		keyByte %= 8;
		tempKey.C = byteToBinary(keyByte / 4);
		keyByte %= 4;
		tempKey.B = byteToBinary(keyByte / 2);
		keyByte %= 2;
		tempKey.A = byteToBinary(keyByte / 1);
		keyByte %= 1;

		return tempKey;
	}

	/**
	 * Converts a byte value to a boolean value.
	 *
	 * @param b
	 *            a given byte value
	 *
	 * @return a boolean value which represents the byte value of
	 *         <strong>b</strong>.
	 */
	public boolean byteToBinary(int b) {
		return b == 0 ? true : false;
	}
}
