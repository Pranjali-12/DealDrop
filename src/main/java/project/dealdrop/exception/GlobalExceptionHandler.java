package project.dealdrop.exception;

import java.net.URI;
import org.springframework.http.HttpHeaders;
import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice 
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private ProblemDetail problem(HttpStatus status,String title,String detail){
        ProblemDetail pd=ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setType(URI.create("about:blank"));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @Override 
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,HttpHeaders headers,HttpStatusCode status,WebRequest request){
        Map<String,String> errors=ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(e->e.getField(), e->e.getDefaultMessage()==null?"invalid":e.getDefaultMessage(),(first,second)->first));

        ProblemDetail pd=problem(HttpStatus.BAD_REQUEST, "Validation Failed", "Request Validation Failed");
        pd.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(pd);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex){
        return problem(HttpStatus.NOT_FOUND,"Resource Not Found",ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ProblemDetail handleNoSuchFound(NoSuchElementException ex){
        return problem(HttpStatus.NOT_FOUND,"Resource Not Found","Requested resiurce was not found");
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicate(DuplicateResourceException ex){
        return problem(HttpStatus.CONFLICT,"Duplicate Resource",ex.getMessage());
    }

    @ExceptionHandler({BadRequestException.class,IllegalArgumentException.class})
    public ProblemDetail handleBadRequest(RuntimeException ex){
        return problem(HttpStatus.BAD_REQUEST,"Bad Request",ex.getMessage());
    }

    @ExceptionHandler (ForbiddenException.class)
    public ProblemDetail handleForbidden(ForbiddenException ex){
        return problem(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage());
    }

    // Covers BadCredentialsException, UsernameNotFoundException wrapped by the AuthenticationManager, etc.
    @ExceptionHandler (AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex){
        return problem(HttpStatus.UNAUTHORIZED, "Authentication Failed", "Invalid email or password");
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwt(JwtException ex) {
        return problem(HttpStatus.UNAUTHORIZED, "Invalid Token", "Invalid or expired JWT token");
    }

    // Comes from method security (@PreAuthorize)
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException ex) {
        return problem(HttpStatus.FORBIDDEN, "Access Denied", "You do not have permission to perform this action");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something went wrong");
    }

    
}
