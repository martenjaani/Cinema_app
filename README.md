# Cinema_app
CGI suvepraktikale kandideerimise ülesanne
Marten Mathias Jaani

## KÄIVITAMINE

1. Klooni repo
2. Navigeeri Frontend kausta
3. käsurealt:
  npm install
  npm run start:both

Frontend jookseb portil 8080 ja backend portil 3000
Vajalik on Java 21, Node.js ja npm

Pärast käskude jooksutamist tasub natuke oodata, kuni backend ja frontend tööle lähevad, siis navigeerida leheküljele localhost:8080

## Dokementatsioon

Tegu on kinoaplikatsiooniga, mis võimaldab kasutajal valida seansi ja osta pileteid. Jõudsin tehtud kõik, mis spetsifikatsioonides oli palutud. Projektile kulus umbes 30h.

Kuna olen õppetöös sarnaste veebiaplikatsioonidega tegelenud, siis otseseselt kuhugi väga kinni ei jäänud. Springboot oli uus mulle, kuid pärast tutoriali läbimist sain asjale üsna kiiresti pihta. Frontendi jaoks kasutasin Vue.js frameworki, sest olen õppetööst sellega tuttav. Siiski pakkus frontend mulle kõige rohkem katsumusi, sest kuigi olin Vue'ga tuttav, siis selle eripärade arusaamine nõudis vahepeal palju debuggimist. Kui jäin kuhugi väga kinni, siis küsisin CoPilotilt abi. Täpsemalt Vue propide korrektne edasi andmine, reaktiivsus, ja sellised eripärad. Kuna mul siiski aega oli vähe, siis ka mõned koodijupid on juturoboti abiga tehtud. Üks sellisteks kohtadest oli istmete soovitamine, kus teadsin kuidas ja millist algoritmi tahan kasutada, ning sain abi sobiliku koodi kirjutamisega. Ka mõned requestid backendis ja fetchid frontendis, mis pole rasked, kuid juturoboti abiga sain need kirjapandud lühemalt ja parema erinditöötlusega. 

## Lehekülje funktsionaalsustest

Koduleheküljel avaneb nimekiri seanssidest. Võimalik on seansse filtreerida kuupäeva, zanri ja vanusepiiranguga. Lisaks on nupp "Soovita vaatamisajaloo põhjal", millele vajutades saad filmisoovitused olenevalt sellest millise zanri filmile oled enim pileti ostnud. Seansi peale vajutades suunatakse uuele leheküljele, kus saab valida istmete arvu. Istmete arvu põhjal valib programm kasutajale optimaalsed isted. Kui vajutada Osta, siis suunatakse tagasi koduleheküljele, ning ostetud pileteid saab vaadata, kui vajutada nuppu "Ostetud Piletid". Seal on nimekiri piletidest seansside kaupa. Kui osteti palju pileteid seansile, siis on seansi-konteineri sees võimalik scrollida ja näha oma kohti. Headeris on veel üks nupp, "Taasta lehekülg algseks", mis eemaldab kasutaja ostetud piletid (ehk ka vaatamisajaloo) ja seansside hõivatud kohtadelt eemaldatakse sinu poolt ostetud kohad.

