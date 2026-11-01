fn main() {
    println!("cargo:rerun-if-changed=src/sculk_ffi.udl");
    match uniffi::generate_scaffolding("src/sculk_ffi.udl") {
        Ok(()) => {}
        Err(error) => {
            eprintln!("uniffi scaffolding generation failed: {error}");
            std::process::exit(1);
        }
    }
}
