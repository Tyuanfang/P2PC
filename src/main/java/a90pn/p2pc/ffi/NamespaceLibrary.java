package a90pn.p2pc.ffi;


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
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_access_token_from_bytes() != ((short) 34102)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_access_token_generate() != ((short) 43109)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_default_inlet_port() != ((short) 27099)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_default_mc_port() != ((short) 23003)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_join_uri_parse() != ((short) 56576)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_lan_broadcaster_start() != ((short) 3888)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_lan_scanner_start() != ((short) 3923)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_data_dir() != ((short) 51215)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_default_host_state_path() != ((short) 62302)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_default_key_path() != ((short) 9461)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_generate_new_key() != ((short) 12188)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_load_host_state() != ((short) 42501)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_load_or_generate_key() != ((short) 33013)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_load_profile() != ((short) 49108)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_load_profile_from() != ((short) 20905)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_save_host_state() != ((short) 59008)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_save_profile() != ((short) 51650)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_persist_save_profile_to() != ((short) 18303)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_probe_server() != ((short) 64760)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_sculpt_node_bind() != ((short) 698)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_secret_key_from_bytes() != ((short) 31465)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_secret_key_generate() != ((short) 23584)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_service_id_from_bytes() != ((short) 61874)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_func_service_id_generate() != ((short) 2277)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_accesstoken_matches() != ((short) 13329)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_accesstoken_to_bytes() != ((short) 43400)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedserviceeventreader_recv() != ((short) 42445)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_join_uri() != ((short) 19270)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_rotate_token() != ((short) 22357)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_service_id() != ((short) 46422)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_status() != ((short) 15334)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_stop() != ((short) 6674)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe() != ((short) 31977)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_subscribe_status() != ((short) 57598)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicehandle_token_state() != ((short) 24384)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_hostedservicestatussubscription_recv() != ((short) 39357)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_joinuri_expose_secret_uri() != ((short) 3338)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_joinuri_redacted() != ((short) 53751)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_joinuri_relay_url() != ((short) 55455)) {
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
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_endpoint_id() != ((short) 50858)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_rotate_token() != ((short) 2679)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_service_count() != ((short) 50150)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_shutdown() != ((short) 10928)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_start_service() != ((short) 50378)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_status() != ((short) 53171)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_sculknode_stop_service() != ((short) 33114)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_secretkey_public_key() != ((short) 25391)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_secretkey_to_bytes() != ((short) 60020)) {
            throw new RuntimeException("UniFFI API checksum mismatch: try cleaning and rebuilding your project");
        }
        if (UniffiLib.uniffi_sculk_ffi_checksum_method_serviceid_to_bytes() != ((short) 55153)) {
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
