<%@ page import="java.util.*" %>

<html>
	<head>
		<meta charset="utf-8" />
        <link rel="stylesheet" href="style.css" />
		<title>Vos commentaires sont-ils utiles ? </title>
	</head>

<body>
   <h1>Vos commentaires sont-ils utiles ?  </h1>
   
   
	<h2> Jouons un peu </h2>
	
	<p>  Ce matin, le facteur est passé et vous a laissé un beau cadeau : le dernier CD de cet 
	artiste méconnu dont vous êtes friand, introuvable ailleurs que sur le net. Quelle joie !
	 Après quelques délicieuses écoutes, il est temps de donner votre avis aux autres internautes
	 sur cet objet, en espérant que votre commentaire soit jugé constructif et puisse aider de 
	 potentiels acheteurs dans leur choix ! </p>

	<h3> Verdict </h3> 
	
	 <% if (request.getAttribute("evaluation")==null) {%>
	 	<p style="color:red"> Un petit problème est survenu... Cliquer <a href="modele?option=ahhh">ici</a> le corrigera.</p>
	 <%} else {  if (request.getAttribute("message") == "Vous n'avez encore rien écrit." ){ %>
				 <p>  Utilitée de votre commentaire : <%= request.getAttribute("evaluation")%>. 
				 <%= request.getAttribute("message")%> </p>
			<% } else { %>
	 
	 
		<p> Vous avez noté ce produit <em style="color:blue"> <%= request.getAttribute("reminderNote")%>  sur 5</em>, avec le texte suivant : </p>
		<p id="retrait"> <%= request.getAttribute("reminderTexte")%></p> 
		 <p> <strong style="color:red"> Utilitée de votre commentaire :  <%= request.getAttribute("evaluation")%>. 
				 <%= request.getAttribute("message")%>  </strong></p>
			 
		 <% } %>
	 <% } %>
	 
	 
	
		<form>
			
            <h3>Note que vous attribuez au produit : </h3>
            
				<SELECT name="note" size="1">
					<OPTION>0
					<OPTION>1
					<OPTION>2
					<OPTION>3
					<OPTION>4
					<OPTION>5
				</SELECT>
			
            <h3>Contenu de votre commentaire:</h3>
			<textarea id="textEntry"  name ="texte" placeholder="Ecrivez ici (en anglais !)"></textarea>
           
		   <p>
		  
				<input type="submit" value="Tester" name="option">
				<input type="reset" value="Remise  à zéro">
				
           </p>
       
		</form>
	
	 <h2> Présentation du projet </h2> 
		<p>
			A l'aide d'une base de données de plus de 60000 commentaires portant sur des CDs provenant 
			d'acheteurs sur Amazon, le but était de construire un modèle prédictif capable 
			d'estimer la qualité d'un commentaire, c'est-à -dire l'utilité que les lecteurs lui trouveront. 
		</p>
		<p> 
			Ci-dessous, les résultats du modèle appliqués à  un sous ensemble de  5000 commentaires. 
			Le premier graphe représente les valeurs estimées par le modèle telles quelles,
			 tandis que pour le deuxième, on a tenu compte du caractère discret de la note de départ.
			L'erreur quadratique moyenne, une fois l'estimation (continue) discrétisée, est de 0.074 pour 
			l'estimation continue, et 0.065 pour l'estimation discrétisée. 
		</p>
	
		<figure> 
			<img src = "2.PNG" alt = "graphe1" width=400px /> <img src = "3.PNG" alt = "graphe2"  width=400px/>
		</figure>
	
	<p class = "remerciements"> Les données utilisées ici le sont avec l'aimable autorisation de J. McAuley;
	voir <a href =  http://jmcauley.ucsd.edu/data/amazon/links.html> ici</a>  pour plus de détails 
	concernant la base de données et d'autres travaux l'ayant utilisée. <p/>
	
	<p> Le modèle statistique a été réalisé sous R et est disponible sur  
	<a href = https://github.com/Toz3wm/Amazon-reviews> GitHub</a>. <p/>
	</body>
</html>
