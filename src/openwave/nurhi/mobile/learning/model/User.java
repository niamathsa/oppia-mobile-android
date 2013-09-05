/* 
 * This file is part of OppiaMobile - http://oppia-mobile.org/
 * 
 * OppiaMobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OppiaMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OppiaMobile. If not, see <http://www.gnu.org/licenses/>.
 */

package openwave.nurhi.mobile.learning.model;

public class User {

	private String username;
	private String email;
	private String password;
	private String phone;
	private String passwordAgain;
	private String firstname;
	private String lastname;
	private String city;
	private String api_key;
	private String professional;
	private String town;
	private String country;
	private String state;
	private String worktype;
	private String currentlyworking;
	private String stafftype;
	private String familyplaning;
	private String nurhitrainning;
	private String education;
	private String religion;
	private String sex;
	private String age;
	private String trainning2;
	// private String professional;

	private int points = 0;
	private int badges = 0;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getPasswordAgain() {
		return passwordAgain;
	}

	public void setPasswordAgain(String passwordAgain) {
		this.passwordAgain = passwordAgain;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getprofessional() {
		return professional;
	}

	public void setprofessional(String professional) {
		this.professional = professional;
	}

	public String gettown() {
		return town;
	}

	public void settown(String town) {
		this.town = town;
	}

	public String getcountry() {
		return country;
	}

	public void setcountry(String country) {
		this.country = country;
	}
	public String getstate() {
		return state;
	}

	public void setstate(String state) {
		this.state = state;
	}

	public String getWorktype() {
		return worktype;
	}

	public String getCity() {
		return city;
	}

	public void setcity(String city) {
		this.lastname = city;
	}

	public void setWorktype(String worktype) {
		this.worktype = worktype;
	}

	public String getcurrentlyworking() {
		return currentlyworking;
	}

	public void setcurrentlyworking(String currentlyworking) {
		this.currentlyworking = currentlyworking;
	}

	public String getstafftype() {
		return stafftype;
	}

	public void setstafftype(String stafftype) {
		this.stafftype = stafftype;
	}

	public String getfamilyplaning() {
		return familyplaning;
	}

	public void setfamilyplaning(String familyplaning) {
		this.familyplaning = familyplaning;
	}

	public String getnurhitrainning() {
		return nurhitrainning;
	}

	public void setnurhitrainning(String nurhitrainning) {
		this.nurhitrainning = nurhitrainning;
	}

	public String geteducation() {
		return education;
	}

	public void seteducation(String education) {
		this.education = education;
	}

	public String getreligion() {
		return religion;
	}

	public void setreligion(String religion) {
		this.religion = religion;
	}

	public String getSex() {
		return sex;
	}

	public void setsex(String sex) {
		this.sex = sex;
	}
	
	public void settrainning2(String training2){
		this.trainning2 = training2;
	}
	
	public String getTrainning2(){
		return trainning2;
	}
	
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
 
	public String getApi_key() {
		return api_key;
	}

	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}

	public String getDisplayName() {
		return firstname + " " + lastname;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getBadges() {
		return badges;
	}

	public void setBadges(int badges) {
		this.badges = badges;
	}

}
