package a90pn.pepc.ffi;


public enum FfiConverterTypeScuLkError implements FfiConverterRustBuffer<ScuLkException> {
    INSTANCE;

    @Override
    public ScuLkException read(java.nio.ByteBuffer buf) {
        return switch(buf.getInt()) {
            case 1 -> new ScuLkException.Busy(FfiConverterString.INSTANCE.read(buf));
            case 2 -> new ScuLkException.NotRunning(FfiConverterString.INSTANCE.read(buf));
            case 3 -> new ScuLkException.InvalidPort(FfiConverterString.INSTANCE.read(buf));
            case 4 -> new ScuLkException.InvalidMaxPlayers(FfiConverterString.INSTANCE.read(buf));
            case 5 -> new ScuLkException.Io(FfiConverterString.INSTANCE.read(buf));
            case 6 -> new ScuLkException.InvalidJoinUri(FfiConverterString.INSTANCE.read(buf));
            case 7 -> new ScuLkException.InvalidEndpoint(FfiConverterString.INSTANCE.read(buf));
            case 8 -> new ScuLkException.AuthorizationDenied(FfiConverterString.INSTANCE.read(buf));
            case 9 -> new ScuLkException.HostUnreachable(FfiConverterString.INSTANCE.read(buf));
            case 10 -> new ScuLkException.TargetUnavailable(FfiConverterString.INSTANCE.read(buf));
            case 11 -> new ScuLkException.LocalPortUnavailable(FfiConverterString.INSTANCE.read(buf));
            case 12 -> new ScuLkException.IdentityUnavailable(FfiConverterString.INSTANCE.read(buf));
            case 13 -> new ScuLkException.OperationConflict(FfiConverterString.INSTANCE.read(buf));
            case 14 -> new ScuLkException.ResourceLimit(FfiConverterString.INSTANCE.read(buf));
            case 15 -> new ScuLkException.InvalidConfiguration(FfiConverterString.INSTANCE.read(buf));
            case 16 -> new ScuLkException.Internal(FfiConverterString.INSTANCE.read(buf));
            case 17 -> new ScuLkException.TargetNotLoopback(FfiConverterString.INSTANCE.read(buf));
            case 18 -> new ScuLkException.InvalidTargetPort(FfiConverterString.INSTANCE.read(buf));
            case 19 -> new ScuLkException.DuplicateService(FfiConverterString.INSTANCE.read(buf));
            case 20 -> new ScuLkException.ServiceNotFound(FfiConverterString.INSTANCE.read(buf));
            case 21 -> new ScuLkException.RotationInProgress(FfiConverterString.INSTANCE.read(buf));
            case 22 -> new ScuLkException.InvalidRefreshPeriod(FfiConverterString.INSTANCE.read(buf));
            case 23 -> new ScuLkException.SessionGenerationExhausted(FfiConverterString.INSTANCE.read(buf));
            case 24 -> new ScuLkException.BindEndpoint(FfiConverterString.INSTANCE.read(buf));
            default -> throw new java.lang.RuntimeException("invalid error enum value, something is very wrong!!");
        };
    }

    @Override
    public long allocationSize(ScuLkException value) {
        return 4L;
    }

    @Override
    public void write(ScuLkException value, java.nio.ByteBuffer buf) {
        switch(value) {
            case ScuLkException.Busy x -> {
                buf.putInt(1);
            }
            case ScuLkException.NotRunning x -> {
                buf.putInt(2);
            }
            case ScuLkException.InvalidPort x -> {
                buf.putInt(3);
            }
            case ScuLkException.InvalidMaxPlayers x -> {
                buf.putInt(4);
            }
            case ScuLkException.Io x -> {
                buf.putInt(5);
            }
            case ScuLkException.InvalidJoinUri x -> {
                buf.putInt(6);
            }
            case ScuLkException.InvalidEndpoint x -> {
                buf.putInt(7);
            }
            case ScuLkException.AuthorizationDenied x -> {
                buf.putInt(8);
            }
            case ScuLkException.HostUnreachable x -> {
                buf.putInt(9);
            }
            case ScuLkException.TargetUnavailable x -> {
                buf.putInt(10);
            }
            case ScuLkException.LocalPortUnavailable x -> {
                buf.putInt(11);
            }
            case ScuLkException.IdentityUnavailable x -> {
                buf.putInt(12);
            }
            case ScuLkException.OperationConflict x -> {
                buf.putInt(13);
            }
            case ScuLkException.ResourceLimit x -> {
                buf.putInt(14);
            }
            case ScuLkException.InvalidConfiguration x -> {
                buf.putInt(15);
            }
            case ScuLkException.Internal x -> {
                buf.putInt(16);
            }
            case ScuLkException.TargetNotLoopback x -> {
                buf.putInt(17);
            }
            case ScuLkException.InvalidTargetPort x -> {
                buf.putInt(18);
            }
            case ScuLkException.DuplicateService x -> {
                buf.putInt(19);
            }
            case ScuLkException.ServiceNotFound x -> {
                buf.putInt(20);
            }
            case ScuLkException.RotationInProgress x -> {
                buf.putInt(21);
            }
            case ScuLkException.InvalidRefreshPeriod x -> {
                buf.putInt(22);
            }
            case ScuLkException.SessionGenerationExhausted x -> {
                buf.putInt(23);
            }
            case ScuLkException.BindEndpoint x -> {
                buf.putInt(24);
            }
            default -> throw new java.lang.RuntimeException("invalid error enum value, something is very wrong!!");
        };
    }
}
