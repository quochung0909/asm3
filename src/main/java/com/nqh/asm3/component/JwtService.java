package com.nqh.asm3.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {
    // Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    private static final String SECERET = "!@#$FDGSDFGSGSGSGSHSHSHSSHGFFDSGSFGSSGHSDFSDFSFSFSFSDFSFSFSF";

    // Phương thức này sẽ sinh ra JWT từ thông tin user
    public String generateToken(String userName){
        // Tạo claims lưu thông tin user 
        Map<String, Objects> claims = new HashMap<>();

        // Trả về jwt cho user 
        return Jwts.builder()
                .setClaims(claims) // Thêm thông tin user
                .setSubject(userName) // Truyền username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian bắt đầu tạo token
                .setExpiration(new Date(System.currentTimeMillis()+ 1000*15*60)) // Thời gian kết thúc token là 60 phút
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact(); // Ký và tạo ra token
    }

    // Phương thức này sẽ kiểm tra tính hợp lệ của token
    private Key getSignKey() {
        // Đoạn mã bí mật này sẽ được giấu ở env, ở đây để minh họa
        byte[] keyBytes = Decoders.BASE64.decode(SECERET);

        // Đổi kiểu của byte[] thành kiểu Key
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Phương thức này sẽ lấy thông tin user từ jwt
    public String extractUserName(String token){

        // Kiểm tra token còn hạn không
        if (isTokenExpired(token)){
            return null;
        } 
        
        // Lấy thông tin user từ token
        return extractClaim(token,Claims::getSubject);
    }

    // Phương thức này sẽ lấy thời gian hết hạn từ token
    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (ExpiredJwtException e) {
            // Token đã hết hạn
            return null;
        } catch (Exception e) {
            // Token không hợp lệ
            return null;
        }
    }

    // Phương thức này sẽ lấy thông tin từ token
    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {

        // Lấy thông tin từ token
        final Claims claims = extractAllClaims(token);

        // Trả về thông tin từ token
        return claimResolver.apply(claims);
    }

    // Phương thức này sẽ lấy thông tin từ token
    private Claims extractAllClaims(String token){
        // Lấy thông tin từ token
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Truyền mã bí mật
                .build() // Tạo ra một đối tượng parser
                .parseClaimsJws(token) // Parse token
                .getBody(); // Lấy thông tin từ token
    }

    // Phương thức này sẽ kiểm tra token có hết hạn không
    private Boolean isTokenExpired(String token){

        // Kiểm tra token có hết hạn không  
        if (extractExpiration(token) == null) {
            return true;
        } else {
            return extractExpiration(token).before(new Date());
        }
    }

    // Phương thức này sẽ kiểm tra token có hợp lệ không
    public Boolean validateToken(String token, UserDetails userDetails){

        // Lấy thông tin user từ token
        final String userName= extractUserName(token);

        // Kiểm tra xem token có hợp lệ không
        return (userName != null && userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
