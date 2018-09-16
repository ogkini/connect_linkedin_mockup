set -x
cd ../../frontend
ng serve --environment prod --aot true --output-hashing all --sourcemaps false --extract-css true --named-chunks false --build-optimizer true
