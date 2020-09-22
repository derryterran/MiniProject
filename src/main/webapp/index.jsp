<html>
<h3><%out.print("Welcome to REST countries"); %></h3>
<br/>
<form action="RESTServletController" method="POST">
<label for="cars">Choose a Service:</label>

<select name="countries" id="countries" onchange="if (this.value=='insertOrUpdateCountry'){this.form['inputJson'].style.visibility='visible';this.form['userEnc'].style.visibility='visible';}else {this.form['inputJson'].style.visibility='hidden';this.form['userEnc'].style.visibility='hidden'};if (this.value=='getChecksumFile'){this.form['ckSum'].style.visibility='visible';this.form['fNm'].style.visibility='visible';}else {this.form['ckSum'].style.visibility='hidden';this.form['fNm'].style.visibility='hidden'};">
  <option value="syncCountries">Sync Countries</option>
  <option value="getCountries">Get Countries</option>
  <option value="insertOrUpdateCountry">Insert or Update country</option>
   <option value="getChecksumFile">Get Checksum File</option>
</select> 
<input type="submit" value="Go" />
<br>
<textarea id="userEnc" name="userEnc" rows="2" cols="50" style="visibility:hidden;">
type your encrypted user ID here
</textarea>
<textarea id="ckSum" name="ckSum" rows="2" cols="30" style="visibility:hidden;">
type your checksum here
</textarea>
<br>
<textarea id="inputJson" name="inputJson" rows="6" cols="50" style="visibility:hidden;">
type your json input here
</textarea>
<textarea id="fNm" name="fNm" rows="2" cols="30" style="visibility:hidden;">
type your filename here
</textarea>
</form>
</html>