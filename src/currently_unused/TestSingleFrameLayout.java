/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package currently_unused;

import currently_unused.IRC_Pane;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;

import ircbot.SettingsPane;

/**
 *
 * @author Garrett
 */
public class TestSingleFrameLayout extends JFrame implements ActionListener{
    // will most likely move to this design once code clean up is complete on IRCBOT.java.
    
    private JPanel mainPanel;
    
    private Vector<IRC_Pane>panes;
    private static JTabbedPane tabbedPane;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem settings;
    private JMenuItem connect;
    private JMenuItem disconnect_m;
    private JMenuItem exit_m;
    private JMenu infoMenu;
    private JMenuItem debug_m;
    private JMenuItem about_m;
    
    protected String currentTitle;
    
    boolean connected = false;
    
    int chatNumber;
    
    private static SettingsPane settingsPane = new SettingsPane();
    
    public TestSingleFrameLayout() {
        chatNumber = 0;
        
        currentTitle = "IRC Client";
        
        URL iconURL = getClass().getResource("P_300x300.png");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        
        mainPanel    = new JPanel(new BorderLayout(5,5));
        
        // menu ---------------------------------------------------
        menuBar       = new JMenuBar();
        fileMenu      = new JMenu("File");
        settings      = new JMenuItem("Settings");
        connect       = new JMenuItem("Connect");
        disconnect_m  = new JMenuItem("Disconnect");
        exit_m        = new JMenuItem("Exit");
        
        infoMenu          = new JMenu("Info");
        debug_m       = new JMenuItem("Debug Window");
        about_m       = new JMenuItem("About");
        
        disconnect_m.setEnabled(false);
        
        settings.addActionListener(this);
        connect.addActionListener(this);
        disconnect_m.addActionListener(this);
        exit_m.addActionListener(this);
        debug_m.addActionListener(this);
        about_m.addActionListener(this);
        
        infoMenu.add(about_m);
        infoMenu.add(debug_m);
        fileMenu.add(settings);
        fileMenu.add(connect);
        fileMenu.add(disconnect_m);
        fileMenu.add(exit_m);
        menuBar.add(fileMenu);
        menuBar.add(infoMenu);
        
        mainPanel.add(menuBar, BorderLayout.PAGE_START);
        
        disconnect_m.setVisible(false);
        // end menu -----------------------------------------------
        
        // tabs ---------------------------------------------------
        tabbedPane = new JTabbedPane();
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        panes = new Vector<IRC_Pane>(1);
        // end tabs -----------------------------------------------
        
        getContentPane().add(mainPanel);
        setSize(900, 600);
        setVisible(true);
        setResizable(true);
        setLocationRelativeTo(null);
        setTitle(currentTitle);
        setMinimumSize(new Dimension(500,400));
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void addPane(boolean type, String name, String label) {
        panes.addElement(new IRC_Pane(type, name, chatNumber));
        tabbedPane.addTab(name, null, panes.lastElement(), label);
        chatNumber++;
    }
    
    public void sendToChat(String text, int chatNum) {
        try {
            panes.elementAt(chatNum).sendToChat(text);
        } catch (IOException ex) {
            Logger.getLogger(TestSingleFrameLayout.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(TestSingleFrameLayout.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        TestSingleFrameLayout a = new TestSingleFrameLayout();
        a.addPane(true, "#psynaps","test tab 1");
        a.addPane(true, "#substarterbottest","test tab 2");
        a.addPane(false, "Raw Data","test tab 3");
        
        
        // test chats
        a.sendToChat("Testing Chat 1...", 0);
        a.sendToChat("Testing Chat 2...", 1);
        a.sendToChat("Testing Raw Data...", 2);
        
        
        // random raw test data
        a.sendToChat(
                "<font color=gray>08:27</font> :tmi.twitch.tv 001 lessaid :Welcome, GLHF!<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv 002 lessaid :Your host is tmi.twitch.tv<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv 003 lessaid :This server is rather new<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv 004 lessaid :-<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv 375 lessaid :-<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv 372 lessaid :You are in a maze of twisty passages, all alike.<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv 376 lessaid :><br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv CAP * ACK :twitch.tv/membership<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv CAP * ACK :twitch.tv/commands<br>" +
                "<font color=gray>08:27</font> :lessaid!lessaid@lessaid.tmi.twitch.tv JOIN #lessaid<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #lessaid :lessaid<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 366 lessaid #lessaid :End of /NAMES list<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv USERSTATE #lessaid<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv ROOMSTATE #lessaid<br>" +
                "<font color=gray>08:27</font> :lessaid!lessaid@lessaid.tmi.twitch.tv JOIN #seriousgaming<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv USERSTATE #seriousgaming<br>" +
                "<font color=gray>08:27</font> :tmi.twitch.tv ROOMSTATE #seriousgaming<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :chrisg2001 danishpow brewsky778 x14hitter rageoverload nanners222 almightygameryt akiller67 xc1utchxpwndx nightblayde morokar mocha_man_12 wckdone gorfenstein runescapekilledwow curwhibbler bullfrogz1111 mrfloppydingus glockoma_o jiffy_ fran_305 viktor_390 bigjalopy thacollecta lquidswarm infinix13 wigge123 bladezuk vcuzzo23 blkwidow30 ridindirty39 xdismantle32 kartia123 calendros koomies imsorrymissjackson saymon26<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :roland_deschain_69 clawxnet lincolnbeard mordisb carnasses jigsawsniper karunnn peebeezrip fletch101 tdog_75 zeoste neonnight34609 kaahi hypeizreal ighnot cokelover__ nygredo brawn_ chimsrage joeyr564 gamernation89 rabbit_gaming dnice3838 alexwolfx3x mrfoxknox audraleigh87 minecraftcrew namelessent1ty starwarssc87 brunettegirl024 kingours spitwod tgkmaki missinperson digiwth jazzi_rogue pijalplue nuggettez heavymetalmak<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :ghostleaderjo jgawarecki camelpuncher assimilate1 eliminatr celegorm001 ledcollector whapper canadianjamz007 deemy20 flaz1k retroenforcer flipdown gregego magicalmeow killsergeant dinsigh survivalserverscom sladin17 ressivgaming fck_tarik creepshowdruid overrustlelogs cr00lik caboosechick satyricon85 mickel07 snackattack_ trmat975 cyberwire69 ajcrill xxanimegodxx lorrec bigdatadude skilz801 midnite121986 thomasr41<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :siujrh dumdumz nerbous godlike75 emdrive sinphonii sagremort bryan610pr asphaltcowboy54 seffykins lordgodn7 kreepr kiriakis77 lazerr89 seraph47 keyito onlockdown66 taylor6757 fanstreaming ejw179 mritpro spider861 concussionzz1122 gettinglucky bluedemonmx clarkerson fredbob213 urban_hero peteythapistol nikg1357 mernster danktank starfallgirl1 camomarley jduggy zutoui82 ohmydinosaur grimmjagger ticompany vipervenom2u<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :gigglyj roldw83 skorpy_ adam_charnes icebox824 nooge_snoogans fenixhaze10 xphaetonx sinlow557 rayvon silywoman1 kismetcat fockalot evilsquid66 beryleva crodex imion saluger tooxehx i3ig_gi233zy mastercrowther deviltrader cass519 jatchley80 gidgy gamer850 slammyjams djquad entri3 miniblazes zzazuu mistrfurious worldofdarkness94 solegs100 punk2k6 theescapologist mweloveyou lothan7 fan_tv_rus budacat krakkantv turboman10<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :lord_kaiba krazirussian grimlin003 twitchwatcher07 howskirama cirril tatsumasa403 cpttoastbrot gumdrops05 costanzathemage kademi24 paracidal xxmaplexx4 inkdalive hagwack swinginjohnson zairus92 xdarkn3cro pitbull7676 raydrecker beedubzftw m1dnight87 abra_kadavar nubadubadub macadamian boatboatboatboat dmentd packers_fann87 thecraziez skillztone snooz781 misterprobinski ialwayssin pal1os supramario101 burnonetwo<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :bravoma07 holyshizzz kaptainkaos327 cassieopia dcain1993 crunchums sdeleon159 marinexicex gamesmeister harrichanchan dualshock_ninja danyellah icezone0 sibon_ollie squiishy_ egyptian_joe polukz trinkiee floobiees mr_s93 swaginmcgleenan123 sneeak buckeyes_26 just_lemon nacho347 beastman567 prfella plutonium_239 calandria_ swedric x_dragula_x iismaboy datpinkpantherdoge boxsetz ucoutlaw mrspankey56 carriontv mattxtz<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :touc87 xrocx2 milkycereal poweredbytea realteamgyro drfeeelgood marpet32 joshing1 thepredator666 sarole andy310 jordandx fozadin relentless_89 deveris sarahjoysokoloff canuck62 pyrethedirtbag aj_k societysmisfit spookygrl vexacus hammyhokjr r34drifter rc_2 lesleystargazer quez420 sinister_snarf arjay1584 shocksnprocs jackiecraft migello91 james_blew001 tomt93 gyranthir feanarok chocotronitv valaqua supractg readablemax<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :hart_sharts_garf briahnanicole moobot m4gnetik chakula dammit_janet seriousgaming amnesia117 ikonstantine2 deepestsam yawgmothlord starkgamer maria8945 graph27 no_fl3x_zone punkifunkimonki kidneykrusader itsmefaxxie cobadoam77 nimssecret absoluticris waxon_waxoff_ shockslayer84 kaiserwilson carebearunicorn j0hnd0g heisengamer alisdriona logiafruit nikthenightowl revlobot lineofire kittymiya amandamarie833 l0wboy<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :fumingmouse1 bloodneverlies dadoug ds82 honey_badger69 morganelizabethx latex_one crypt135 mrlolrus bubbagump4 digital_zeus1236 mab0913 alansshartedjeans babybluefromheaven hungle1207 mowf_gaming grippenn odintitanius neps415 belore babyjo95 bloodycountess heavystep casualgerm emmberz orionhak robin207 twixxor jaheirak canonelite princezpeach denalieu jkingdom ravle joydecoy vapidfuzzballhd hession96 kronichawk katstar1<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :annowyn dj_blakout moogleson flyingsquash sinnercide maddog316uk der_pandemie becc7 whileycoyote7 getrektmikebrown michaelgon88 lollipopshop soulrevolver superfood zooith smallpair winterstorm33 stargale civilxii eyez2412 krieger_the_great deeooo25 centaurianx burlyoctane xasunt hunterdemons chilblain knarp theredwolf14 captnau adamscuh gokussan mdgun kurfew serch9k saiko689 araxxis cmonstre marabouone googa71<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :poppasmurff__85 jazpur arandomusertoo sh00t2k1ll86 zanzum79 mutedbrian tekudroid gloriance dreece5 calliopz fiverlivegaming biggamer247 hakarthan matthalleck demgamerz45 clb86clb serosen kobekeyto squizz_tv murtalll syn1st3r barconus jonnist youjustgotfaced wardem0n airnath jaggerzs biggz222 grizzlybear111 ili_me_ili sir_keyzer_soze ghostories rwthegreat88 atmosfferee skywalkerzero lavilio joefoo kanikulypoljus gvbuy<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :doodle197638 memirzade ajbk4life1 godless vinylscratchwubs4 simonbpuk vslater ecsenius nephurusamyae iftox theblev soulstitch101 jordanmkasla2009 sarimnerone hauru87 xvolo jenzemann nachohustler minn3apolis xon_eg jonny0093 ooryanukoo xzero7d3 murducky pandalaser13 fcqt im_a_nool3 landict micahthebrave invisus46 mittons1441 swiftlerr djmikey1 snoopdog6969 zoestrasza loopmottin burnsshadow bstephenson1225 nerdcuber<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :xlemmy bamf_smash puffgothesmoke kronostheofficer cornnu badkarma86 kedea74 mikangabi gnuur thrust0 xscarletdoll thecitadel88 hiddeninsanity shaniajoy nesrain_ monkeybeasts spaceghost8 octavyus ironhidel caylara111 ajboomhauer thunderlilly twogage linuara89 thekidmusic palidmist centralqt gamermamma patricksquatch yourprecious1 lurch__ maximus0815 rorrespinosa 3rdtime 2commodus thelegend288 invisobel neoash kojak488<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :nick8961 streamline1175 katyprecious kronosnxs maxypadd hugo___r midknightnate assassinatedchicken stoop143 d_rock27 starliiive supernaut88 dragonfearz the_coz lindseykay21 dangerusssnowman xjinxx zyngor alb55 tommygottagun ossler diggyguwop22 cp4600 gzus81 andy725511 munra420 conflictionkilrogg hawkeye__gb sardoniscorn furty7 hearthston3d slapnuts77 velsharoon ltcaire iamdidi squaresoft12 sulfur21 aydizzzz soggle<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :biofreak69 dizzygrenade fire_eater64 mrk_gamingtribe lela_23 diemess_tv ghostmentor doctor_montalban jwamburgey parjk1 zygo16 rayskizi02 cheriees joshnnz michaelpaul74 kyneaz super_dragon_ fizzlefack stoney253 filthdog michiiiy dsemaj fujiboo ssnakesii superbrotendo dream_weavrr legitamato colossus69 averyfu bestbuds337 xzxnarutoxzx undonebirdman51 ejmetzner barak_rahamim hurl3 pollyfuse itsr2ghgaming ellcar kawaii<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :bluetip84 stoictaint ladylay90 grickshaft hotshotpin21 obsessedd wildzingz alossar hecticspaniard pz_recon carverebain energy329 halomaster2400 melancholy225 hawaiianflame<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 353 lessaid = #seriousgaming :lessaid<br>" +
                "<font color=gray>08:27</font> :lessaid.tmi.twitch.tv 366 lessaid #seriousgaming :End of /NAMES list<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o joeyr564<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o 3rdtime<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o kartia123<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o crodex<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o djmikey1<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o jkingdom<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o doctor_montalban<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o revlobot<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o hunterdemons<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o kawaii<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o survivalserverscom<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o inkdalive<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o moobot<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o conflictionkilrogg<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o jiffy_<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o chilblain<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o youjustgotfaced<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o itsmefaxxie<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o seriousgaming<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o boxsetz<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o cass519<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o emmberz<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o jordanmkasla2009<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o nanners222<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o centralqt<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o shockslayer84<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o nikg1357<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o seraph47<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o ikonstantine2<br>" +
                "<font color=gray>08:27</font> :jtv MODE #seriousgaming +o tommygottagun<br>" +
                "<font color=gray>08:27</font> :superbrotendo!superbrotendo@superbrotendo.tmi.twitch.tv PRIVMSG #seriousgaming :Star Wars like 2 1/4 hours<br>" +
                "<font color=gray>08:27</font> :ighnot!ighnot@ighnot.tmi.twitch.tv PRIVMSG #seriousgaming :aha!<br>" +
                "<font color=gray>08:27</font> :monkey_d_notorious!monkey_d_notorious@monkey_d_notorious.tmi.twitch.tv PRIVMSG #seriousgaming :Hello @Seriousgaming and the always wonderful chat!<br>" +
                "<font color=gray>08:27</font> :grizzlybear111!grizzlybear111@grizzlybear111.tmi.twitch.tv PRIVMSG #seriousgaming :That smile tho<br>"

                , 2);
        
        //for(int x = 0; x < 90000; x++) {
        //    a.sendToChat("Testing Chat 1......................................................................................................... " + x, 0);
        //}
    }
    
    public void setWindowTitle(String newTitle) {
        if(!currentTitle.equals(newTitle)) {
            setTitle(newTitle);
            currentTitle = newTitle;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == exit_m) {
            //makeNewWindow("Window " + frameCounter, JFrame.DISPOSE_ON_CLOSE, 800, 500, 2);
            //settingsPane.showPane();
            System.exit(0);
        }
        else if (e.getSource() == connect) {
            if(!connected) {
                connect.setEnabled(false);
                disconnect_m.setEnabled(true);
                connect.setVisible(false);
                disconnect_m.setVisible(true);
                connected = true;
            }
        }
        else if (e.getSource() == disconnect_m) {
            if(connected) {
                connect.setEnabled(true);
                disconnect_m.setEnabled(false);
                connect.setVisible(true);
                disconnect_m.setVisible(false);
                connected = false;
            }
        }
        
        else if(e.getSource() == settings) {
            settingsPane.showPane();
        }
    }
}
