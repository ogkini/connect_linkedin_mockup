# Install node-modules (no '-g' here, as we don't want to make global changes)
cd frontend
npm install
npm install @angular/cli@1.7.4	# We use Angular5
npm install --save jwt-decode
# Add more libraries here, if needed.
