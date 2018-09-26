set -x
cd ../../frontend
ng serve --proxy-config proxy.conf.json —-ssl true --environment prod --aot true --output-hashing all --sourcemaps false --extract-css true --named-chunks false --build-optimizer true
