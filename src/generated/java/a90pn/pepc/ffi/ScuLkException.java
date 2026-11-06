package a90pn.pepc.ffi;



/**
 * 胶水层统一错误。
 */
public class ScuLkException extends java.lang.Exception {
    private ScuLkException(java.lang.String message) {
      super(message);
    }

    public static class Busy extends ScuLkException {
      public Busy(java.lang.String message) {
        super(message);
      }
    }
    public static class NotRunning extends ScuLkException {
      public NotRunning(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidPort extends ScuLkException {
      public InvalidPort(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidMaxPlayers extends ScuLkException {
      public InvalidMaxPlayers(java.lang.String message) {
        super(message);
      }
    }
    public static class Io extends ScuLkException {
      public Io(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidJoinUri extends ScuLkException {
      public InvalidJoinUri(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidEndpoint extends ScuLkException {
      public InvalidEndpoint(java.lang.String message) {
        super(message);
      }
    }
    public static class AuthorizationDenied extends ScuLkException {
      public AuthorizationDenied(java.lang.String message) {
        super(message);
      }
    }
    public static class HostUnreachable extends ScuLkException {
      public HostUnreachable(java.lang.String message) {
        super(message);
      }
    }
    public static class TargetUnavailable extends ScuLkException {
      public TargetUnavailable(java.lang.String message) {
        super(message);
      }
    }
    public static class LocalPortUnavailable extends ScuLkException {
      public LocalPortUnavailable(java.lang.String message) {
        super(message);
      }
    }
    public static class IdentityUnavailable extends ScuLkException {
      public IdentityUnavailable(java.lang.String message) {
        super(message);
      }
    }
    public static class OperationConflict extends ScuLkException {
      public OperationConflict(java.lang.String message) {
        super(message);
      }
    }
    public static class ResourceLimit extends ScuLkException {
      public ResourceLimit(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidConfiguration extends ScuLkException {
      public InvalidConfiguration(java.lang.String message) {
        super(message);
      }
    }
    public static class Internal extends ScuLkException {
      public Internal(java.lang.String message) {
        super(message);
      }
    }
    public static class TargetNotLoopback extends ScuLkException {
      public TargetNotLoopback(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidTargetPort extends ScuLkException {
      public InvalidTargetPort(java.lang.String message) {
        super(message);
      }
    }
    public static class DuplicateService extends ScuLkException {
      public DuplicateService(java.lang.String message) {
        super(message);
      }
    }
    public static class ServiceNotFound extends ScuLkException {
      public ServiceNotFound(java.lang.String message) {
        super(message);
      }
    }
    public static class RotationInProgress extends ScuLkException {
      public RotationInProgress(java.lang.String message) {
        super(message);
      }
    }
    public static class InvalidRefreshPeriod extends ScuLkException {
      public InvalidRefreshPeriod(java.lang.String message) {
        super(message);
      }
    }
    public static class SessionGenerationExhausted extends ScuLkException {
      public SessionGenerationExhausted(java.lang.String message) {
        super(message);
      }
    }
    public static class BindEndpoint extends ScuLkException {
      public BindEndpoint(java.lang.String message) {
        super(message);
      }
    }
    
}

