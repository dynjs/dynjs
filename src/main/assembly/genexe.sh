#!/bin/sh
echo "Creating dynjs executable"
(echo '#!/bin/sh\nexec java -jar $0 "$@"\n'; cat target/dynjs-all.jar) > dynjs && chmod +x dynjs
