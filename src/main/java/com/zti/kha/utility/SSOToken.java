package com.zti.kha.utility;

/**
 * Created by Windows 8.1 on 30/10/2561.
 */

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import com.zti.kha.api.CommonApi;
import com.zti.kha.model.Common.LocalizeText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;


public class SSOToken extends CommonApi {
    static Logger log = LogManager.getLogger(SSOToken.class);

    static LocalizeText localizeText;



        public static String create( String userInfo,String secret,long expire,String issuer){

            String token = null;
            final long iat = System.currentTimeMillis() / 1000L; // issued at claim
            final long exp = iat + expire; // expires claim.
            final JWTSigner signer = new JWTSigner(secret);
            Map<String, Object> claims = new HashMap<String, Object>();
            claims.put("iss",issuer);
            claims.put("exp", exp);
            claims.put("iat", iat);
            claims.put("key", userInfo);
            token = signer.sign(claims);
            return token;
        }

        /**
         * Decode token to sso model.
         * @param token
         * @return
         */
        public static Object decode(String token ,String secret) throws PostExceptions {
            localizeText= new LocalizeText();

            Object model = null;
            Map<String, Object> claims;
            final JWTVerifier verifier = new JWTVerifier(secret);
            try {
                if(token != null ){
                    claims = verifier.verify(token);
                    model = claims.get("key");
                }
            }
            catch (IllegalStateException ex){
//                log.error("Token error : " + ex.getMessage());
//                ex.printStackTrace();
            }
            catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (SignatureException e) {
                // TODO Auto-generated catch block

//                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
//                e.printStackTrace();
            } catch (JWTVerifyException e) {
                // TODO Auto-generated catch block
                throw new PostExceptions(FAILED, localizeText.getTokenExpire());
            }finally{

            }
            return model;

        }

//	public static UserCustOffiDtl getUserFromToken(String token){
//		String json = (String)SSOToken.decode(token, "model");
//		UserCustOffiDtl sso = null;
//		if(json != null){
//			sso = new Gson().fromJson(json, UserCustOffiDtl.class);
//		}
//		return sso;
//	}
    }
