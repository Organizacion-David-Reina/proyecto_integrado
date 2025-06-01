package com.proyectointegrado.reina_cabrera_david.encryption;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Utility class for generating, storing, and retrieving RSA key pairs.
 * This class ensures the existence of a directory to store keys, generates RSA
 * public/private key pairs, and provides methods to retrieve them. Keys are
 * serialized and stored in the local file system for persistent use.
 */
public class KeyStoreHelper {

	// Directory and file paths for storing RSA keys
	private static final String KEYS_DIR = "src/main/resources/keys";
	private static final String PRIVATE_KEY_FILE = KEYS_DIR + "/private.key";
	private static final String PUBLIC_KEY_FILE = KEYS_DIR + "/public.key";

	// RSA algorithm and key size configuration
	private static final String KEY_ALGORITHM = "RSA";
	private static final int KEY_SIZE = 2048;

	/**
	 * Generates and stores an RSA key pair (private and public keys).
	 * If the keys directory does not exist, it is created. The private key
	 * is stored in `private.key` and the public key in `public.key`.
	 *
	 * @throws Exception if an error occurs during key generation or storage
	 */
	public static void generateAndStoreKeys() throws Exception {
		// Ensure the directory for storing keys exists
		ensureKeysDirectoryExists();

		// Generate a new RSA key pair
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGenerator.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		// Serialize and store the private key
		try (ObjectOutputStream privateKeyOut = new ObjectOutputStream(new FileOutputStream(PRIVATE_KEY_FILE))) {
			privateKeyOut.writeObject(keyPair.getPrivate());
		}

		// Serialize and store the public key
		try (ObjectOutputStream publicKeyOut = new ObjectOutputStream(new FileOutputStream(PUBLIC_KEY_FILE))) {
			publicKeyOut.writeObject(keyPair.getPublic());
		}
	}

	/**
	 * Ensures that the directory for storing keys exists, creating it if necessary.
	 */
	private static void ensureKeysDirectoryExists() {
		File keysDir = new File(KEYS_DIR);
		if (!keysDir.exists()) {
			keysDir.mkdirs();
		}
	}

	/**
	 * Retrieves the RSA private key from the stored file.
	 * If the private key file does not exist, a new key pair is generated and stored.
	 *
	 * @return the RSA private key
	 * @throws Exception if an error occurs during key retrieval or key generation
	 */
	public static PrivateKey getPrivateKey() throws Exception {
		File privateKeyFile = new File(PRIVATE_KEY_FILE);

		// Generate and store keys if the private key file is missing
		if (!privateKeyFile.exists()) {
			generateAndStoreKeys();
		}

		// Deserialize and return the private key
		try (ObjectInputStream privateKeyIn = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE))) {
			return (PrivateKey) privateKeyIn.readObject();
		}
	}

	/**
	 * Retrieves the RSA public key from the stored file.
	 * If the public key file does not exist, a new key pair is generated and stored.
	 *
	 * @return the RSA public key
	 * @throws Exception if an error occurs during key retrieval or key generation
	 */
	public static PublicKey getPublicKey() throws Exception {
		File publicKeyFile = new File(PUBLIC_KEY_FILE);

		// Generate and store keys if the public key file is missing
		if (!publicKeyFile.exists()) {
			generateAndStoreKeys();
		}

		// Deserialize and return the public key
		try (ObjectInputStream publicKeyIn = new ObjectInputStream(new FileInputStream(publicKeyFile))) {
			return (PublicKey) publicKeyIn.readObject();
		}
	}
}
