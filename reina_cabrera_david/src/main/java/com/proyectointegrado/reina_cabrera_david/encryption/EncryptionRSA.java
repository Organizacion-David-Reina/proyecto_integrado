package com.proyectointegrado.reina_cabrera_david.encryption;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * Utility class for performing RSA encryption and decryption operations.
 * This class is responsible for securely encrypting messages using a public key
 * and decrypting them with a private key. It uses the "RSA/ECB/PKCS1Padding"
 * transformation for RSA operations and encodes/decodes messages in Base64 format.
 */
public class EncryptionRSA {

	/**
	 * Encrypts a plain text message using the provided RSA public key.
	 * This method takes a plain text input, encrypts it using the public key, and
	 * returns the resulting encrypted message encoded in Base64 format for safe
	 * transmission or storage.
	 *
	 * @param mensajeNormal the plain text message to be encrypted
	 * @param publicKey the RSA public key used to encrypt the message
	 * @return the encrypted message encoded in Base64
	 * @throws Exception if an error occurs during the encryption process
	 */
	public static String encrypt(String mensajeNormal, PublicKey publicKey) throws Exception {
		// Initialize the RSA cipher in encryption mode
		Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);

		// Perform encryption and encode the result in Base64
		byte[] encryptedMessage = rsaCipher.doFinal(mensajeNormal.getBytes());
		return Base64.getEncoder().encodeToString(encryptedMessage);
	}

	/**
	 * Decrypts an encrypted message using the provided RSA private key.
	 * This method takes a Base64-encoded encrypted message, decodes it to bytes,
	 * and decrypts it using the private key. The resulting plain text is returned
	 * as a string.
	 *
	 * @param encryptedMessage the Base64-encoded encrypted message
	 * @param privateKey the RSA private key used to decrypt the message
	 * @return the decrypted plain text message
	 * @throws Exception if an error occurs during the decryption process
	 */
	public static String decrypt(String encryptedMessage, PrivateKey privateKey) throws Exception {
		// Decode the Base64-encoded message to bytes
		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);

		// Initialize the RSA cipher in decryption mode
		Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);

		// Perform decryption and convert the result to a string
		byte[] decryptedMessage = rsaCipher.doFinal(encryptedBytes);
		return new String(decryptedMessage);
	}
}
