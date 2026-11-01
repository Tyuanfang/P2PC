package a90pn.pepc.ffi;


final class NamespaceLibrary {
    static synchronized String findLibraryName(String componentName) {
        String libOverride = System.getProperty("uniffi.component." + componentName + ".libraryOverride");
        if (libOverride != null) {
            return libOverride;
        }
        return "sculk_ffi";
    }

    static java.lang.foreign.SymbolLookup loadLibrary() {
        String name = findLibraryName("sculk_ffi");
        if (name.startsWith("/") // Unix absolute path
                || name.startsWith("\\\\") // Windows UNC path
                || (name.length() > 2 && name.charAt(1) == ':')) // Windows drive path (e.g. C:\)
        {
            System.load(name);
        } else {
            System.loadLibrary(name);
        }
        return java.lang.foreign.SymbolLookup.loaderLookup();
    }

    static void uniffiCheckContractApiVersion() {
        int bindingsContractVersion = 30;
        int scaffoldingContractVersion = UniffiLib.ffi_sculk_ffi_uniffi_contract_version();
        if (bindingsContractVersion != scaffoldingContractVersion) {
            throw new RuntimeException("UniFFI contract version mismatch: try cleaning and rebuilding your project");
        }
    }
    static void uniffiCheckApiChecksums() {
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_default_inlet_port() != ((short) 27099)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_default_mc_port() != ((short) 23003)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_lan_broadcaster_start() != ((short) 3888)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_lan_scanner_start() != ((short) 3923)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_probe_server() != ((short) 64760)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_lanbroadcaster_is_finished() != ((short) 48421)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_lanbroadcaster_stop() != ((short) 32622)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_lanscanner_is_finished() != ((short) 61457)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_lanscanner_stop() != ((short) 26076)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_lanscanner_try_recv() != ((short) 18424)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_subscription_recv() != ((short) 29564)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_tunnelservice_shutdown() != ((short) 26367)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_tunnelservice_start_host() != ((short) 27421)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_tunnelservice_start_join() != ((short) 56619)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_tunnelservice_status() != ((short) 61194)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_tunnelservice_stop() != ((short) 19683)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_tunnelservice_subscribe() != ((short) 19729)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_constructor_tunnelservice_new() != ((short) 8699)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
    }
}

// Define FFI callback types
