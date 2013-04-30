#!/bin/sh
echo "Creating dynjs executable"
(echo '#!/usr/bin/env java -jar'; cat target/dynjs-all.jar) > dynjs && chmod +x dynjs
