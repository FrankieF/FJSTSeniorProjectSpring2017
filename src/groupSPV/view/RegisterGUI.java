package groupSPV.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;

import groupSPV.controller.LoginList;
import groupSPV.controller.Utils;

/** RegisterGUI facilitates new user creation.
 * @author Frank Fasola
 * @author James Donnell
 * @author Spencer Escalante
 * @author Trevor Silva */
public class RegisterGUI extends JFrame {
	
	private static final long serialVersionUID = 1354289425581234145L;
	
	/** JButton for user interaction. */
	private JButton canceButton, registerBtn;
	/** JLabel for user information. */
	private JLabel enterPasswordLabel, enterUserNameLabel, jLabel1, verifyPasswordLabel;
	/** JPasswordField for obscured password input. */
	private JPasswordField password, verifyPassword;
	/** JTextField for username input. */
	private JTextField username;

	/** Creates new form RegisterGUI */
	public RegisterGUI() {
		/* Set Nimbus look and feel. */
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException exception) {
			java.util.logging.Logger.getLogger(LoginGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, exception);
		}
		initComponents();
		Utils.setWindowCenterOfScreen(this);
		setVisible(true);
	}

	/** This method is called from within the constructor to initialize the form. */
	private void initComponents() {
		jLabel1 = new JLabel();
		username = new JTextField();
		password = new JPasswordField();
		verifyPassword = new JPasswordField();
		enterUserNameLabel = new JLabel();
		enterPasswordLabel = new JLabel();
		verifyPasswordLabel = new JLabel();
		canceButton = new JButton();
		registerBtn = new JButton();

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
		jLabel1.setText("Register an Account");

		enterUserNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		enterUserNameLabel.setText("Enter a Username:");

		enterPasswordLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		enterPasswordLabel.setText("Enter a Password:");

		verifyPasswordLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		verifyPasswordLabel.setText("Verify Password:");

		canceButton.setText("Cancel");
		canceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		registerBtn.setText("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				registerBtnActionPerformed(evt);
			}
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addComponent(jLabel1)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addGroup(layout
										.createParallelGroup(GroupLayout.Alignment.TRAILING).addGroup(layout
												.createSequentialGroup().addComponent(enterUserNameLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 5,
														Short.MAX_VALUE)
												.addComponent(username, GroupLayout.PREFERRED_SIZE, 190,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup().addComponent(enterPasswordLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(password, GroupLayout.PREFERRED_SIZE, 190,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup().addComponent(verifyPasswordLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(layout
														.createParallelGroup(GroupLayout.Alignment.LEADING,
																false)
														.addComponent(verifyPassword,
																GroupLayout.PREFERRED_SIZE, 190,
																GroupLayout.PREFERRED_SIZE)
														.addGroup(layout.createSequentialGroup()
																.addComponent(canceButton)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.RELATED,
																		GroupLayout.DEFAULT_SIZE,
																		Short.MAX_VALUE)
																.addComponent(registerBtn)))))
								.addGap(91, 91, 91)))));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(28, 28, 28).addComponent(jLabel1)
						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(username, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(enterUserNameLabel))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(password, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(enterPasswordLabel))
						.addGap(18, 18, 18)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(verifyPassword, GroupLayout.PREFERRED_SIZE, 28,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(verifyPasswordLabel))
						.addGap(28, 28, 28)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(canceButton).addComponent(registerBtn))
						.addContainerGap(33, Short.MAX_VALUE)));

		getRootPane().setDefaultButton(registerBtn);
		pack();
	}

	/** Attempts to create the entered User.
	 * @param evt Not-utilized ActionEvent. */
	private void registerBtnActionPerformed(ActionEvent evt) {
		String passwordString = new String(password.getPassword());
		if(!username.getText().equals("") && !passwordString.equals("") && passwordString.equals(new String(verifyPassword.getPassword())) && LoginList.exists(username.getText())== false) {
			LoginList.addUser(username.getText(), passwordString);
			dispose();
		}else{
			JOptionPane.showMessageDialog(null,"User Exists or Fields Are Null", "Registration Error",JOptionPane.ERROR_MESSAGE);
			username.setText("");
			password.setText("");
			verifyPassword.setText("");
		}
	}

	/** Closes the RegisterGUI.
	 * @param evt Not-utilized ActionEvent. */
	private void cancelButtonActionPerformed(ActionEvent evt) {
		dispose();
	}
}
