package model;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@WebServlet("/modele")
public class ModeleStatistique extends HttpServlet {
	
	public static final String INITIALMESSAGE = "Vous n'avez encore rien écrit.";
	private static final String MESSAGE0 = "Votre commentaire n'est pas assez long, ou ne contient pas assez de mots signifiants";
	private static final String MESSAGE025 = "On ne peut pas plaire à tout le monde...";
	private static final String MESSAGE05 = "C'est un bon début.";
	private static final String MESSAGE075 = "Encore un petit effort et votre sagesse sera reconnue.";
	private static final String MESSAGE090 = "On peut le dire : votre avis pèse dans la balance !";
	private static final String MESSAGE1 = "Vous devriez donner des cours de commentaire.";
	private static final String MESSAGEERROR ="Il semblerait qu'il y ait une erreur... Mille excuses !";
	private static final String[] WORDSLIST = { "album", "awesom", "band", "best", "better", "classic", "ever", "excel", "fan", "favorit", "get", "good", "great", "greatest", "hit", "just", "like", "love", "masterpiec", "music", "must", "one", "record", "rock", "song", "sound", "star", "still", "time"};
	private static final int WORDNUMBER = 29;
	private static final double[] coefficients = { -2.149007, 0.015156729, 0.480223066, -0.182077198, 0.240782226, 0.134159367, 0.474030148, 0.167083973, 0.0425067, -1.547083734, 0.996454023, 0.101117979, 0.042562353, -0.959542476, -0.151560798, 0.292716948, -1.635368795, 0.31378075, -0.208660821, -0.447323438, -0.266888651, -1.048182271, -0.483353, -0.270883452, 0.127074782, 0, 0.419791763, -1.176810673, -0.045561802, 0.277732031, 0.533422544, -0.28803663, 0.00316883, 0.00116136, 0.076463022, -0.051627273, 0.061290395, 0.061723322, 0.008264769, 0.089103604, -0.099127667, 0.084649816, 0.100665549, 0.048780281, 0.013960305, -0.094242531, 0.111349321, 0.108248816, -0.058051829, 0.096533551, 0.062302004, 0.10096692, 0.035300671, -0.109331293, 0.007703577, -0.087549231, -0.010142765, -0.026068415, 0.034083324, 0.010198097, -0.074931434, 0.083897941, 0.036427965, -0.00767871, -0.000425526, -0.029544458, 0.012517164, -0.000771498, -0.008364527, -0.011278781, -0.001762947, 0.014379516, 0.013091988, -0.026162009, -0.00791494, -0.009868585, 0.042264346, -0.010271808, -0.023902392, 0.033487459, -0.012870541, 0.001915052, -0.001393621, 0.022904996, 0.026336029, 0.014362638, 0.039461665, -0.004978237, 0.020980905, -0.021751311, 0.034988071, 0.038679589, -0.023696714, -0.004045024, 0.01494356, -0.002809149, 0.000923143, 0.001743592, -0.003012605, -0.001437578, -0.00147182, -0.001305368, 0.004756904, -0.00606864, -0.003159638, 0.000491638, -0.007663636, -0.002772659, 0.000406245, -0.00134849, -0.005626403, -0.003560643, -0.004738743, -0.007532517, -0.001769163, -0.005828675, -0.003946764, -0.000270598, -0.005858074, 0.003289099, 0.004330672, -0.00352199, 0.002054419, -0.004899876, -0.005507306, 0.052517502, -0.08000814, -0.012458811, -0.081075014, -0.078288349, 0.052387468, 0.259821328, -0.186959524, -0.034645641, -0.002828672, 0.277553326, 0.000574582, -0.103864382, 0.27633828, -0.084836245, 0.050127375, 0.075202396, 0.06098917, 0.27375456, 0.120004197, 0.079586702, -0.014878137, 0.080511045, -0.094016816, 0.143663941, -0.022465218, -0.093417508, -0.092194072, 0.060205617, 0.191898502, 0.201310864, -0.0000296, -0.190202992, 0.25548352, -0.087261934, 0.05548281, -0.20884734, 0.112173792, -0.142079519, 0.088344424, 0.007582213, 0.257481419, 0.031128391, -0.355229385, 0.349301667, 0.012256517, -1.227179662, -0.224631543, 0.163055028, -0.122810643, -0.877954407, 0.090801918, -0.111544629, -0.190685269, 0.090413291, 0.21299918, -0.081214542, -0.251016512, -0.317602312, 1.093143189, -0.051336426, 12.98281508, 0, 0, 0, -15.22366496, 13.20092668, -0.535622032, 12.15316508, 0.214530178, 0.779549671, -26.9896185, 13.14963077, 12.84724806, 1.636529243, 0.296344258, -0.642191416, 0.283862387, 0.389208964, 13.48546878, -0.006417587, -2.325947334, 0.173928545, -0.137992273, -0.314483913, -0.214495928, -0.185290896, -0.286804507, 0.2956342, 0.127055097, -0.409453032, 0.343726137, -0.531194959, 0.029327469, -0.296912463, -0.012529402, -0.526415172, -0.347721447, -1.034957775, -0.834754234, 0.135154829, 1.246125244, 0.39019076, 0.272177536, 0.375278485, 1.073648295, 0.910730275, -0.552847036, -0.378996963, 0.194799652, -0.029180233, 0.103697303, -0.010759324, 0.767027436, 1.380396056, 1.239504621, -0.630310984, -0.050344897, 0.02742669, 0.259889228, 0.323239493, 0.634599106, -0.298060569, -0.035516594, 8.633744893, -0.027796497, 0.279025491, 0.119925972, -0.489901062, -0.171679011, -0.250719478, 0.5959033, -0.227828819, 0.449295133, 0.066753555, 0.539286135, -0.383833302, 11.76089884, -1.819317088, 0.247901139, 0.301464969, -0.271945113, -0.088331711, -14.78224817, 1.500052517, 0.064140933, 1.681430585, 0.121434629, 0.366995422, -0.693648405, 1.298257798, 0.025919524, -0.353041199, -0.640084324, -0.124657321, -0.080235088, 0.416478198, 0.142365476, 0.35219775, -0.711469006, 0.538251707, 0.533275484, -0.478018638, 0.435532706, -0.242810146, 0.282058932, 1.190471759, -0.007111051, 12.82748191, 0.417514779, 0.4869098, -0.242917645, -0.425343088, 0.017199752, -0.250452212, -0.222116511, -0.061993523, 0.020904187, -0.024105294, 0.175560689, -0.422316202, -0.228882247, -0.74510064, -0.449361869, -0.724420374, -0.053445966, -0.228806172, 0.537166056, -0.327604875, 0.921121521, -1.115890989, 0.166274615, 3.741665151, 13.38292015, 0.67796351, -1.853413954, 0.227091966, 0.161431622, -0.050997318, 0.463528531, -0.095113255, 0.004147512, 0.127018285, -0.69172116, 1.062848085, 0.924004056, -0.124925021, 0.724838089, -0.368087329, -0.577075172, 0.219550757, -0.950796508, 0.624501236, -0.119035295, -0.081239795, 0.023458413, 1.961863245, -0.504798125, 0.360076495, 0.519844942, -0.070528846, 0.412864697, -13.52150896, -0.455965192, -0.051811505, 1.096521926, 1.9456977, 0.110690656, 0.022173917, 31.44270379, 12.77337957, 0.787027153, -0.727891619, 0.380818886, -0.045959836, 0.639926304, 0.317149913, -0.320236392, -0.646142765, -0.05596506, -0.612223677, 0.798265578, 0, 0.329830988, 0.083444622, 12.83784525, 0.090621341, 0.166200798, 13.49964336, 0, -0.148462157, 1.246492481, -0.841735195, 0, 0.00306159, 13.73705341, 0.034062447, 0.094666083, 1.615052881, 0.727542334, 1.330106865, -1.022428479, 0.049767346, -0.002331857, -0.124772974, -0.00161908, -0.318201287, -0.003753722, -0.822833548, 0.512554227, 2.391568759, -0.582907222, 2.119175238, 0.146463696, 0.665796917, -1.083970804, -1.22204094, 1.681858041, -1.234419701, -0.05037584, 12.97659341, -0.13205878, -0.062284028, -0.224379577, -0.287165344, 0.568738862, 0.16592488, 0.85697438, 0.193267095, -0.00369964, 0.677165416, 0.044536549, -0.004241412, -0.030836696, 0.075291992, 0.076017242, -0.054508645, 0.120626572, 0.087956484, -0.046853716, 0.145576517, 0.136812442, -0.195576498, -0.103718561, 0.55403149, 0.064121859, -0.155720811, 0.128274979, -0.063865827, 0.317660849, 0.132871578, 0.174215107, -0.201989154, 0.026666429, 0.237115053, 0.414162102, -0.020010782, -1.289324006, 0, 0.052845533, 0.312837426, 13.14357753, 0.266360585, -0.041588959, -0.099325753, -0.107928037, 2.006077317, 0.619696884, 0.930174491, -0.012485623, 0.491401222, 0.600634071, 0.057545498, 0, 0.132703505, 0, -0.34696827, 0.576143733, 0.382914186, 0.340161895, 10.63945648, -0.000404602, -0.149450675, 1.239407934, -0.161410964, -0.330704813, -14.08771991, -0.782160566, -0.560499529, -0.247470316, -0.925518594, 0.470589518, 0.064524267, -0.421366181, -0.208305821, 1.372944444, 0.121436974, 0.212795234, 1.509960585, -0.034261229, 0.29409767, -0.01110875, 0.295146367, 0.032805988, 0.789663929, 0.138169807, 0.530924137, 0.47617193, 0.094782975, 0.874849076, 0.293473185, -0.53642244, -0.083337377, -0.170356228, 0.143121493, -0.140744595, 0.516811936, -0.689682178, -0.231692405, 0.283138673, 0.309522959, -0.751240122, -0.477118629, -0.894025252, 0.417930542, 0.175156194, -0.140149149, 0.326089579, 0.125544183, -0.405387866, -0.159738934, 0.379193321, -0.589246307, 0.018819322, -0.47340428, 0.069614714, -0.130654295, 0.572595258, 0.495509919, -0.29827571, 0.541361694, -0.248172679, -0.827616579, -0.097693468, 1.660619666, -0.901960868, -1.159230393, -0.149324663, 0.202766016, -0.110530769, 0.358491458, 0.058402173, 0.207478506, 0.157134317, -0.346945494, 0.811090558, -0.550198122, -0.301012092, -0.165445592, 1.022662932, -0.331434177, 0.668507527, -0.143359694, 0.087420366, -0.14623248, -0.277854586, 0.342913052, -0.367584343, -0.363609314, 0.072710939, 0.243516062, -0.617023976, 0.319098116, 0.626090798, 0.49259023};
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("doGet	");
		String option = request.getParameter("option");
		System.out.println(option);
		if(option.equals("Tester")){
			double evaluation = calculeNote(request);
			String msg;
			if (evaluation == 0){
				msg = MESSAGE0;
			} else if (evaluation <= 0.25) {
				msg = MESSAGE025;
			} else if (evaluation <= 0.5) {
				msg = MESSAGE05;
			} else if (evaluation <= 0.75) {
				msg = MESSAGE075;
			} else if (evaluation <= 0.90) {
				msg = MESSAGE090;
			} else if (evaluation <= 1 ){
				msg = MESSAGE1;
			} else {
				evaluation = 0;
				msg = MESSAGEERROR;
			}
			request.setAttribute("message", msg);
			String evalFormat = String.format("%.3f\n", evaluation); 
			request.setAttribute("evaluation", evalFormat);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		} else {
			request.setAttribute("message", INITIALMESSAGE);
			request.setAttribute("evaluation", 0);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
		}
		}
	

	
	
	
	public double calculeNote(HttpServletRequest request){
		String texte = request.getParameter("texte");
		String snote = request.getParameter("note");
		request.setAttribute("reminderNote", snote);
		request.setAttribute("reminderTexte", texte);
		System.out.println(snote + "  text  : " + texte);
		int V1 = 2;
		int V2 = 4;
		
		double logresult = 0;
		int length = texte.length();
		int variables[] = new int[coefficients.length];
		texte = texte.toLowerCase();
		int  c = 0;
		variables[c++] = 1;
		variables[c++] = length;
		variables[c++] = Integer.parseInt(snote);
		for (int i = 0; i < WORDNUMBER; i++){
			variables[c++] = stringOccur(texte, WORDSLIST[i]);
		}
		variables[c++] = V1*V2;
		for (int i = 0; i < WORDNUMBER+2; i++){
			variables[c++] = V1*variables[i+1];
		}
		for (int i = 0; i < WORDNUMBER+2; i++){
			variables[c++] = V2*variables[i+1];
		}
		for(int i = 0; i < WORDNUMBER; i++){
			for(int j = i+1; j < WORDNUMBER; j++){
				variables[c++] = variables[i]*variables[j];
			}
		}
		
		for(int k = 0; k < variables.length; k++){
			logresult += variables[k]*coefficients[k];
		}
		
		double result = Math.exp(logresult)/(1+Math.exp(logresult));
		if(texte == ""){
			result = 0;
		}
		System.out.println("le résultat vaut " +result+ " et le texte "+texte + " aa "+(texte==null));
		return result;
	}
	
	 public final int stringOccur(String text, String string) {
		    return regexOccur(text, Pattern.quote(string));
		}

		
		 public final int regexOccur(String text, String regex) {
		    Matcher matcher = Pattern.compile(regex).matcher(text);
		    int occur = 0;
		    while(matcher.find()) {
		        occur ++;
		    }
		    return occur;
		}
}
	

