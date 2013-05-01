#!/bin/sh
echo "Creating dynjs executable"
if [ ! -d "bin" ] 
then
  mkdir -v "bin"
fi
(echo '#!/bin/sh\nexec java -jar $0 "$@"\n'; cat target/dynjs-all.jar) > bin/dynjs && chmod +x bin/dynjs
